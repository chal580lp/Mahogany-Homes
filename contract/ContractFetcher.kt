package com.recursive.mahoganyhomes.contract

import com.recursive.mahoganyhomes.Bot
import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.common.hoverAndDI
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import com.runemate.game.api.hybrid.region.Npcs
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.hybrid.util.Regex
import com.runemate.game.api.script.Execution

class ContractFetcher(private val bot: Bot)  {
	private val log = getLogger("ContractFetcher")
	var lastContractor: Contractor? = null

	fun getNewContract(tier: Int): Boolean {
		// Find nearest/preferred contract giver
		val contractGiver = Npcs.newQuery().names(*Contractor.names()).results().nearest()
		if (contractGiver != null && contractGiver.distanceTo(Players.getLocal()) <= 6 && contractGiver.position?.isReachable == true) {
			bot.updateStatus("Getting new contract from: ${contractGiver.name}")
			lastContractor = Contractor.closestToPlayer()
			if (!ChatDialog.isOpen()) {
				hoverAndDI(contractGiver, "Contract")
				Execution.delayUntil({ ChatDialog.isOpen() }, { Players.getLocal()?.isMoving }, 1200)
			}

			return selectContractTier(tier)
		} else {
			if (bot.isFaladorOnComplete) {
				bot.updateStatus("Fetching new contract from Falador")
				bot.navigateTo(Contractor.AMY.location)
			} else {
				bot.updateStatus("Fetching new contract from closest contractor")
				bot.navigateTo(Contractor.closestToPlayer().location)
			}
			return false
		}
	}


	private fun selectContractTier(tier: Int): Boolean {
		// Select appropriate contract tier based on level and preference
		val tierOption = when(tier) {
			1 -> "Beginner"
			2 -> "Novice"
			3 -> "Adept"
			4 -> "Expert"
			else -> null
		}

		if (tierOption != null && ChatDialog.isOpen()) {
			ChatDialog.getOption(Regex.getPatternForContainsString(tierOption))?.select() ?: ChatDialog.getContinue()?.select()
			return true
		}

		return false
	}
}
