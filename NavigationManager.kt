package com.recursive.mahoganyhomes

import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.home.Houses
import com.recursive.mahoganyhomes.home.MahoganyHome
import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.local.House
import com.runemate.game.api.hybrid.local.MouseCamera
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.location.navigation.Path
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.osrs.local.hud.interfaces.Magic
import com.runemate.game.api.script.Execution
import com.runemate.pathfinder.Pathfinder
import com.runemate.pathfinder.model.POH

class NavigationManager(private val bot: Bot, private val pathfinder: Pathfinder) {
	private val log = getLogger("NavigationManager")


	companion object {
		private val HOSIDIUS_AREA = Area.Rectangular(Coordinate(1857, 3646, 0), Coordinate(1709, 3512, 0))
		private val VARROCK_AREA = Area.Rectangular(Coordinate(3136, 3517, 0), Coordinate(3301, 3373, 0))
		private val ROSS_HOUSE = Area.Rectangular(Coordinate(2611, 3318, 0), Coordinate(2617, 3315, 0))
	}


	fun navigateToHouse(mahoganyHome: MahoganyHome) {
		val player = Players.getLocal() ?: return
		if (mahoganyHome in Houses.HOSIDIOUS_HOUSES) {
			val playerCoordinate = player.position ?: return
			if (House.isInside()) {
				val portal = GameObjects.newQuery().names("Portal").actions("Enter").results().firstOrNull()
				if (portal == null) {
					bot.stopAndPause("Unable to find Portal to exit House")
					return
				}
				portal.interact("Enter") && Execution.delayUntil({ !House.isInside() }, { player.isIdle }, 2400)
				return
			}
			if (!HOSIDIUS_AREA.contains(playerCoordinate) && bot.hosidiusTeleportMethod in setOf("Runes", "Tab")) {
				if (House.getCurrent() != House.Location.KOUREND) bot.stopAndPause("Please move house to Kourend")
				if (!Bank.close()) return
				hosidiusHouseTeleport()
				return
			}
		}
		else if (mahoganyHome in Houses.VARROCK_HOUSES) {
			if (VARROCK_AREA.contains(player)) {
				navigateTo(mahoganyHome.location, false)
				return
			}
		}
		val destination = mahoganyHome.location
		navigateTo(destination)
	}

	fun navigateTo(destination: Coordinate, enableTeleports: Boolean = true) {
		if (!Bank.close()) return
		if (ROSS_HOUSE.contains(Players.getLocal())) {
			exitRossHouse()
			return
		}
		val path = pathfinder.lastPath?.takeIf {
			it.isValid && it.last.position?.let { last -> last.distanceTo(destination) < 5 } == true
		} ?: buildNewPath(destination, enableTeleports)

		path?.let {
			if (it.step()) {
				Execution.delay(600, 900)
			}
		}
	}

	private fun buildNewPath(destination: Coordinate, enableTeleports: Boolean = true): Path? {
		log.info("Building new path dest: $destination")
		val lastPath = pathfinder.lastPath
		log.warn("Last path valid: ${lastPath?.isValid} dest:${lastPath?.last?.position} ${lastPath?.length}")
		return pathfinder.pathBuilder()
			.preferAccuracy()
			.enableCharterShips(enableTeleports)
			.destination(destination)
			.enableHomeTeleport(enableTeleports)
			.poh(POH.builder().build())
			.enableTeleports(enableTeleports)
			.enableSpellTeleports(enableTeleports)
			.findPath()
	}

	private fun hosidiusHouseTeleport(): Boolean {
		val tab = Inventory.newQuery().names("Teleport to house").actions("Outside").results()
		if (!tab.isEmpty()) {
			return tab.firstOrNull()?.let { it.interact("Outside") } == true && Execution.delayUntil(
				{ HOSIDIUS_AREA.contains(Players.getLocal()) },
				{ Players.getLocal()?.animationId != -1 },
				4000
			)
		}

		return Magic.TELEPORT_TO_HOUSE.activate("Outside") && Execution.delayUntil(
			{ HOSIDIUS_AREA.contains(Players.getLocal()) },
			{ Players.getLocal()?.animationId != -1 },
			4000
		)
	}

	private fun exitRossHouse() {
		bot.updateStatus("Exiting Ross's House")
		log.info("Manually exiting Ross's House")
		val player = Players.getLocal() ?: return
		GameObjects.newQuery()
			.names("Door")
			.types(GameObject.Type.BOUNDARY)
			.on(Coordinate(2610, 3316, 0))
			.results()
			.firstOrNull()?.let {
				MouseCamera.turnTo(it)
				if (it.interact("Open")) {
					Execution.delayUntil({ !ROSS_HOUSE.contains(player) }, { !player.isIdle }, 1600)
				}
			} ?: log.error("Failed to find Door in Ross's House")
	}

}