package com.recursive.mahoganyhomes.contract

import com.recursive.mahoganyhomes.Bot
import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.home.FurnitureSlot
import com.recursive.mahoganyhomes.home.Houses
import com.recursive.mahoganyhomes.home.MahoganyHome
import com.runemate.game.api.hybrid.local.Varbits
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox
import com.runemate.game.api.script.framework.listeners.ChatboxListener
import com.runemate.game.api.script.framework.listeners.events.MessageEvent

class ContractStateTracker(private val bot: Bot): ChatboxListener {
	private val log = getLogger("ContractStateTracker")

	var currentMahoganyHome: MahoganyHome? = null
		private set

	var contractTier: Int = 0
		private set

	var sessionContracts: Int = 0
		private set

	var sessionPoints: Int = 0
		private set

	var lastContractor: Contractor? = null

	// State flags
	private var contractFinished = false
	private var contractAssigned = false

	private fun handleChatMessage(message: String) {
		when {
			CONTRACT_FINISHED.matches(message) -> {
				log.info("Contract finished detected")
				sessionContracts++
				sessionPoints += getPointsForCompletingTask()
				currentMahoganyHome = null
				contractFinished = true
				contractAssigned = false
				//bot.worldMap.animateHouseComplete()
			}
			CONTRACT_ASSIGNED.matches(message) -> {
				log.info("New contract assigned")
				log.error(message)
				CONTRACT_ASSIGNED.find(message)?.let { match ->
					val tier = match.groupValues[1]
					val houseName = match.groupValues[2]
					setContractTierFromString(tier)
					setHouseFromOwnerName(houseName)
				} ?: log.error("Could not parse contract assigned message")
				contractAssigned = true
				contractFinished = false
			}
			CONTRACT_PATTERN.matches(message) -> {
				log.info("Contract instructions received")
				CONTRACT_PATTERN.find(message)?.let { match ->
					val houseName = match.groupValues[2]
					log.info("House owner: $houseName")
				} ?: log.error("Could not parse contract message")
			}
			REMINDER_PATTERN.find(message)?.let { match ->
				log.info("Contract reminder received")
				val tier = match.groupValues[1]
				val houseName = match.groupValues[2]
				setContractTierFromString(tier)
				setHouseFromOwnerName(houseName)
				log.info("Reminder - Tier: $tier, House owner: $houseName")
			} != null -> {}
		}
	}

	fun setHouse(mahoganyHome: MahoganyHome) {
		currentMahoganyHome = mahoganyHome
		log.info("Set current house to ${mahoganyHome.owner}")
		//bot.worldMap.selectHouseByOwner(mahoganyHome.owner)
	}

	private fun setHouseFromOwnerName(ownerName: String) {
		val house = Houses.ALL_HOUSES.find { it.owner.equals(ownerName, ignoreCase = true) }
		if (house != null) {
			setHouse(house)
		} else {
			log.error("Could not find house for owner: $ownerName")
		}
	}

	private fun setContractTierFromString(tier: String) {
		contractTier = when(tier.lowercase()) {
			"beginner" -> 1
			"novice" -> 2
			"adept" -> 3
			"expert" -> 4
			else -> 0
		}
		log.info("Set contract tier to $contractTier")
	}

	private fun getPointsForCompletingTask(): Int {
		// Contracts reward 2-5 points depending on tier
		return contractTier + 1
	}

	fun needsNewContract(): Boolean {
		return currentMahoganyHome == null && (!ChatDialog.isOpen() || bot.atHouse == null)
	}

	fun hasFinishedCurrentHouse(): Boolean {
		return currentMahoganyHome?.let { house ->
			getCompletedCount(house) == 0
		} ?: false
	}

	private fun getCompletedCount(mahoganyHome: MahoganyHome): Int {
		val startingVarb = FurnitureSlot.SLOT_1.varb
		val houseObjects = mahoganyHome.houseObjects.objects

		return houseObjects.indices.count { index ->
			val varbValue = Varbits.load(startingVarb + index)?.value ?: 0
			varbValue == 1 || varbValue == 3 || varbValue == 4
		}
	}


	fun reset() {
		currentMahoganyHome = null
		contractTier = 0
		contractFinished = false
		contractAssigned = false
		log.info("Reset contract state")
	}

	companion object {
		// Regex patterns for contract states
		//07:05:52 [DEBUG] [ContractStateTracker] MessageEvent(type=Server, sender=, message=Beginner Contract: Go see Sarah along the south wall of Varrock.)
		//07:54:48 [DEBUG] [ContractStateTracker] MessageEvent(type=Server, sender=, message=Beginner Contract: Go see Bob in north-east Varrock, opposite the church.)
		private val CONTRACT_PATTERN = Regex("(Please could you g|G)o see (\\w*)[ ,][\\w\\s,-]*[?.] You can get another job once you have furnished \\w* home\\.")
		//private val REMINDER_PATTERN = Regex("You're currently on an (\\w*) Contract\\. Go see (\\w*)[ ,][\\w\\s,-]*\\. You can get another job once you have furnished \\w* home\\.")
		private val REMINDER_PATTERN = Regex("currently on an? (\\w+) Contract.*Go see (\\w+)")
		private val CONTRACT_FINISHED = Regex("You have completed [\\d,]* contracts with a total of [\\d,]* points?\\.")
		private val CONTRACT_ASSIGNED = Regex("(\\w*) Contract: Go see (\\w*)(?:,|\\s+).*")
		private val REQUEST_CONTRACT_TIER = Regex("Could I have an? (\\w*) contract please\\?")
	}

	override fun onMessageReceived(p0: MessageEvent?) {
		if (p0 == null) return
		if (p0.type == Chatbox.Message.Type.SERVER || p0.type == Chatbox.Message.Type.UNKNOWN) {
			p0.message?.let {
				handleChatMessage(it)
				logContractorMessage(p0)
			}
		}

	}

	private fun logContractorMessage(event: MessageEvent) {
		if (event.speaker in listOf("Amy", "Angelo", "Ellie", "Marlo")) {
			log.warn("SPEAKER")
		}
		lastContractor = Contractor.closestToPlayer()
		log.info("Last contracter: $lastContractor")
		log.debug(event)
	}

	fun setContractTier(selectedTier: Int) {
		log.info("Manually setting contract tier to $selectedTier")
		contractTier = selectedTier
	}
}