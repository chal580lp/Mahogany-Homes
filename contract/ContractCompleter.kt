package com.recursive.mahoganyhomes.contract

import com.recursive.mahoganyhomes.FurnitureFinder
import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.home.MahoganyHome
import com.recursive.mahoganyhomes.common.hoverAndDI
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import com.runemate.game.api.hybrid.location.navigation.Traversal
import com.runemate.game.api.hybrid.location.navigation.cognizant.ScenePath
import com.runemate.game.api.hybrid.region.Npcs
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.hybrid.util.calculations.Distance
import com.runemate.game.api.script.Execution

class ContractCompleter {
	private val log = getLogger("ContractCompleter")
	private var mahoganyHome: MahoganyHome? = null
	private val MIN_ENERGY = 35 // Configure as needed

	fun complete(currentMahoganyHome: MahoganyHome): Boolean {
		mahoganyHome = currentMahoganyHome
		val npc = findHouseNpc()
		if (npc == null) {
			log.error("Could not find NPC")
			mahoganyHome?.let { FurnitureFinder.climbLadder(it) }
			return false
		}

		if (npc.position?.isReachable == false) {
			log.error("NPC is not reachable")
			mahoganyHome?.let { FurnitureFinder.openDoor(npc) }
			return false
		}

		// If NPC is too far, walk closer
		if (!npc.isVisible || Distance.between(Players.getLocal(), npc) > 10) {
			log.info("Walking to NPC")
			ScenePath.buildTo(npc)?.step()
			Execution.delay(600)
			return false
		}

		// Interact with NPC
		if (!ChatDialog.isOpen()) {
			log.info("Talking to NPC")
			hoverAndDI(npc, "Talk-to")
			if (!Execution.delayUntil({ ChatDialog.isOpen() }, { Players.getLocal()?.isMoving }, 1600))
				return false
		}
		log.debug("Continuing chat")
		ChatDialog.getContinue()?.select()
		return true
	}

	private fun findHouseNpc(): Npc? {
		return mahoganyHome?.let { h ->
			Npcs.newQuery()
				.ids(h.npcId)
				.results()
				.nearest()
		}
	}

	private fun needsEnergyRestore(): Boolean {
		return Traversal.getRunEnergy() < MIN_ENERGY
	}
}