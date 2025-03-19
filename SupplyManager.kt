package com.recursive.mahoganyhomes

import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.contract.ContractStateTracker
import com.recursive.mahoganyhomes.contract.Contractor
import com.recursive.mahoganyhomes.home.MahoganyHome
import com.recursive.mahoganyhomes.item.PlankSack
import com.recursive.mahoganyhomes.pathing.HousePathing
import com.recursive.mahoganyhomes.pathing.HousePaths
import com.runemate.game.api.hybrid.entities.Player
import com.runemate.game.api.hybrid.local.MouseCamera
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.region.Banks
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution

class SupplyManager(
	private val bot: Bot,
	private val cst: ContractStateTracker,
	private val sack: PlankSack,
	private val nm: NavigationManager,
) {
	var needToBank = false
	private val plankList = listOf("Plank", "Oak plank", "Teak plank", "Mahogany plank")
	private fun getPlankType(selectedTier: Int): String {
		return when (selectedTier) {
			1 -> "Plank"
			2 -> "Oak plank"
			3 -> "Teak plank"
			4 -> "Mahogany plank"
			else -> "No such tier"
		}
	}

	private fun getUnselectedPlankTypes(selectedTier: Int): List<String> {
		return plankList.filter { it != getPlankType(selectedTier) }
	}

	private val log = getLogger("SupplyManager")
	private fun needsMoreSupplies(selectedTier: Int): Boolean {
		val house = cst.currentMahoganyHome ?: return false
		val requiredPlanks = house.getRequiredPlanks(selectedTier)
		val requiredSteelBars = house.getRequiredSteelBars(selectedTier)
		val invPlanks = Inventory.getQuantity(getPlankType(selectedTier)) + sack.getTierPlankCount(selectedTier)
		val invSteelBars = Inventory.getQuantity("Steel bar")
		return invPlanks < requiredPlanks
				|| invSteelBars < requiredSteelBars
				|| Bank.isOpen() && !Inventory.isFull()
	}

	private fun withdrawSupplies(selectedTier: Int) {
		val invSteelBars = Inventory.getQuantity("Steel bar")
		val plankType = getPlankType(selectedTier)
		val unSelectedPlankTypes = getUnselectedPlankTypes(selectedTier).toTypedArray()
		val unSelectedPlanks = Inventory.newQuery().names(*unSelectedPlankTypes).results().toList()
		if (unSelectedPlanks.isNotEmpty()) {
			bot.updateStatus("Depositing all ${unSelectedPlanks.first().definition?.name} to bank")
			val plank = unSelectedPlanks.first()
			log.info("Depositing all ${plank.definition?.name} to bank")
			Bank.deposit(plank, 0)
		} else if (invSteelBars <= 1 && Inventory.getEmptySlots() != 0) {
			bot.updateStatus("Withdrawing steel bars")
			log.info("Withdrawing steel bars")
			if (Bank.getQuantity("Steel bar") <= 2) {
				bot.stopAndPause("Not enough Steel Bars in bank")
				return
			}
			Bank.withdraw("Steel bar", 2 - invSteelBars)
		} else if (Inventory.getEmptySlots() != 0) {
			bot.updateStatus("Withdrawing $plankType")
			log.info("Withdrawing planks for tier $selectedTier")
			if (Bank.getQuantity(plankType) <= 5) {
				bot.stopAndPause("Not enough $plankType in bank")
				return
			}
			Bank.withdraw(plankType, -1)
		} else {
			if (Inventory.containsAllOf(plankType, "Steel bar")) {
				log.info("Planks and steel bars are in inventory, no longer need to bank")
				needToBank = false
			}
		}
	}

	private fun handleOptimalBanking(shouldBank: Boolean, house: MahoganyHome?): Boolean {
		house ?: return false
		val lastContractor = bot.contractStateTracker.lastContractor ?: return false
		val closestContractor = Contractor.closestToPlayer()
		val optimalPath = HousePaths.getHousePath(lastContractor, house)
		optimalPath?.let { path ->
			log.info("Optimal path for ${house.owner} is ${path.fasterOption()} last: ${lastContractor.gameName}")
			if ((path.fasterOption() == HousePathing.Option.WALK_TP_HOUSE &&
						closestContractor == lastContractor) ||
				path.isSameCityPath(closestContractor)) {
				if (shouldBank) {
					log.debug("Walk To Bank ${house.owner} - Walk: ${path.contractorToBankToTPToHouse} TP: ${path.teleportToBankToHouse}")
					log.info("Walking to bank setting needToBank to True ${if (path.isSameCityPath(closestContractor)) "Same City" else ""}")
					needToBank = true
					return true
				}
			} else {
				if (closestContractor != lastContractor) {
					if (shouldBank) {
						log.debug("Teleport to Bank${house.owner} - Walk: ${path.contractorToBankToTPToHouse} TP: ${path.teleportToBankToHouse}")
						log.info("Have TPed setting needToBank to True")
						needToBank = true
						return true
					}
				}
			}
		}
		return false
	}


	private fun checkIfNeedToBank(): Boolean {
		//val player = Players.getLocal() ?: return false
		if (cst.contractTier == 0) return false
		val house = cst.currentMahoganyHome
		if (bot.atHouse != null && bot.atHouse == house) return false
		val shouldBank = needsMoreSupplies(cst.contractTier)
		val nearBank = bot.closestBankDistance <= 15
		if (nearBank && Inventory.getEmptySlots() >= 11) {
			needToBank = true
			return true
		}
		if (handleOptimalBanking(shouldBank, house)) return true
		return false
		//val offsite = house != null && player.distanceTo(house.location) < 200 && !house.area.contains(player, true)
		//if (!offsite) return false
		needToBank = true
		log.debug("Need to bank set to true")
		return true
	}

	fun handleBanking(): Boolean {
		if (!needToBank) {
			return checkIfNeedToBank()
		}
		val player = Players.getLocal() ?: return false
		log.debug("Banking dist: ${bot.closestBankDistance}")
		if (Bank.isOpen()) {
			withdrawSupplies(cst.contractTier)
		} else {
			bot.updateStatus("Heading to Bank")
			BankLocation.getClosestTo(player.position)?.let { bankLoc ->
				if (player.distanceTo(bankLoc) > 7) {
					nm.navigateTo(bankLoc)
				} else {
					openBank(player)
				}
			}
		}
		return true
	}

	private fun openBank(player: Player) {

		Banks.newQuery().results().nearest()?.let { banker ->
			if (!banker.isVisible) MouseCamera.turnTo(banker)
			banker.hover()
			if (banker.interact("Bank")) {
				Execution.delayUntil({ Bank.isOpen() }, { player.isMoving }, 1600)
			}
		}
	}

	fun fillPlankSack(): Boolean {
		if (!sack.hasChecked || sack.isFull() || !Bank.isOpen() || bot.selectedContractTier != cst.contractTier) return false
		val hasPlanks = Inventory.contains(getPlankType(bot.selectedContractTier))
		if (!hasPlanks) return false
		sack.fill()
		return true
	}

	fun emptyPlankSack(): Boolean {
		if (!sack.hasWrongTierPlanks(bot.selectedContractTier) || !Bank.isOpen()) return false
		Inventory.newQuery().names(*plankList.toTypedArray()).results().first()?.let {
			log.info("Depositing ${it.definition?.name} to bank")
			Bank.deposit(it, 0)
			return true
		}
		sack.empty()
		return true
	}
}