package com.recursive.mahoganyhomes

import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.common.hoverAndDI
import com.recursive.mahoganyhomes.game.RepairableObject
import com.recursive.mahoganyhomes.home.FurnitureSlot
import com.recursive.mahoganyhomes.home.FurnitureType
import com.recursive.mahoganyhomes.home.Houses
import com.recursive.mahoganyhomes.home.MahoganyHome
import com.recursive.mahoganyhomes.home.MahoganyHome.Companion.getHouseByCoordinate
import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.entities.LocatableEntity
import com.runemate.game.api.hybrid.local.Camera
import com.runemate.game.api.hybrid.local.Varbits
import com.runemate.game.api.hybrid.region.GameObjects
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.hybrid.util.Regex
import com.runemate.game.api.script.Execution

class FurnitureFinder(private val furnitureTracker: FurnitureTracker) {

	private val lastStairsId = -1

	fun getVarbNeededActions(mahoganyHome: MahoganyHome): List<Triple<Int, Int, String>> {
		val startingVarb = FurnitureSlot.SLOT_1.varb
		val neededActions = mutableListOf<Triple<Int, Int, String>>()  // objectId, varbValue, action

		mahoganyHome.houseObjects.objects.forEachIndexed { index, obj ->
			val varbValue = Varbits.load(startingVarb + index)?.value ?: 0

			when (varbValue) {
				1 -> neededActions.add(Triple(obj.objectId, varbValue, "repair"))
				3 -> neededActions.add(Triple(obj.objectId, varbValue, "remove"))
				4 -> neededActions.add(Triple(obj.objectId, varbValue, "build"))
			}
		}

		neededActions.sortBy { (objId, _, _) ->
			val id = mahoganyHome.houseObjects.getOrderOfObject(objId)
			if (id == Int.MAX_VALUE) {
				log.error("Object $objId has no order")
			}
			id
		}

		if (neededActions.isNotEmpty()) {
			val repairsNeeded = neededActions.filter { it.third == "repair" }.size
			val removesNeeded = neededActions.filter { it.third == "remove" }.size
			val buildsNeeded = neededActions.filter { it.third == "build" }.size
//			log.debug("Found ${neededActions.size} objects - ${if (repairsNeeded > 0) " $repairsNeeded repairs" else ""} ${if (removesNeeded > 0) " $removesNeeded removes" else ""} ${if (buildsNeeded > 0) " $buildsNeeded builds" else ""}")
//			log.debug("Found ${neededActions.size} objects needing attention: ${neededActions.map { it.third }}")
//			neededActions.forEach { (objId, _, action) ->
//				log.debug("Object $objId needs $action")
//			}
		}

		return neededActions
	}


	fun getNextRepairableObject(mahoganyHome: MahoganyHome): RepairableObject? {
		val playerPlane = Players.getLocal()?.position?.plane ?: return null

		// Get all objects that need attention according to varbs
		val neededActions = getVarbNeededActions(mahoganyHome)
//		furnitureTracker.processVarbActions(house, neededActions)
		if (neededActions.isEmpty()) {
			log.info("No objects need attention according to varbs")
			return null
		}

		// Try to find any object from our needed actions list on current plane
		val allObjects = GameObjects.newQuery()
			.ids(*neededActions.map { it.first }.toIntArray())
			.filter { obj -> obj.position?.plane == playerPlane }
			.results()
			.sortedBy {			obj ->
				val id = mahoganyHome.houseObjects.getOrderOfObject(obj.id)
				id }
		val foundObject = allObjects.firstOrNull()
		log.info("Selected object: $foundObject order: ${foundObject?.id?.let {
			mahoganyHome.houseObjects.getOrderOfObject(
				it
			)
		}}")

		allObjects.forEach { obj ->
			logObject(obj)
		}

		// If we can't find any objects that need work on this plane
		if (foundObject == null) {
			log.info("No objects found on plane $playerPlane, checking for stairs")
			// Try stairs if we have any defined
			return findNearestStairs(mahoganyHome)?.let { stairs ->
//				log.info("Found stairs at ${stairs.position}")
				RepairableObject(
					gameObject = stairs,
					type = FurnitureType.BUILD_1,
					action = "climb"
				)
			}
		}

		// Find what needs doing with this object
		val objectIndex = mahoganyHome.houseObjects.objects.indexOfFirst { it.objectId == foundObject.id }
		if (objectIndex == -1) {
			log.error("Found object but couldn't match to house definition")
			return null
		}

		val varbValue = Varbits.load(FurnitureSlot.SLOT_1.varb + objectIndex)?.value ?: 0
		val action = when (varbValue) {
			1 -> "repair"
			3 -> "remove"
			4 -> "build"
			else -> return null
		}

//		log.info("Found object to $action at ${foundObject.position}")
		return RepairableObject(
			gameObject = foundObject,
			type = mahoganyHome.houseObjects.objects[objectIndex].type,
			action = action
		)
	}

	private fun logObject(obj: GameObject?) {
//		log.debug("{} distance {}|{}  at {} {}",
//			obj?.id,
//			obj?.distanceTo(Players.getLocal()),
//			obj?.position,
//			obj?.position?.x,
//			obj?.position?.y
//		)
	}

	private fun findNoellaStairs(
		mahoganyHome: MahoganyHome,
		neededActions: List<Triple<Int, Int, String>>,
	): GameObject? {
		val leftRoomObjects = setOf(40160, 40161, 40162, 40163) // L Drawer, L Tables, L Clock
		val rightRoomObjects = setOf(40156, 40157, 40158, 40159) // R Dresser, R Cupboard, R Hat Stand, R Mirror

		// Check which room has objects needing attention
		val needsLeftRoom = neededActions.any { (objId, _, _) -> leftRoomObjects.contains(objId) }
		val needsRightRoom = neededActions.any { (objId, _, _) -> rightRoomObjects.contains(objId) }

		// If we only need one room, use the appropriate stairs
		val stairIds = when {
			needsLeftRoom -> setOf(17026, 16685)
			needsRightRoom && !needsLeftRoom -> setOf(15645, 15648)
			else -> mahoganyHome.ladders // If we need both rooms or neither, use all stairs
		}

		return GameObjects.newQuery()
			.ids(*stairIds.toIntArray())
			.results()
			.nearest()
	}

	private fun findNearestStairs(mahoganyHome: MahoganyHome): GameObject? {
		if (mahoganyHome.ladders.isEmpty()) return null

		// Get all objects that need attention
		val neededActions = getVarbNeededActions(mahoganyHome)
		if (neededActions.isEmpty()) return null

		// Special handling for Noella's house
		if (mahoganyHome == Houses.NOELLA && Players.getLocal()?.position?.plane == 0) {
			return findNoellaStairs(mahoganyHome, neededActions)
		}

		// For other houses, use all available stairs
		return GameObjects.newQuery()
			.ids(*mahoganyHome.ladders.toIntArray())
			.results()
			.nearest()
	}

	companion object {
		private val log = getLogger("FurnitureFinder")
		fun climbLadder(mahoganyHome: MahoganyHome): Boolean {
			val ladder =
				GameObjects.newQuery().ids(*mahoganyHome.ladders.toIntArray()).results().nearest() ?: return false
			if (!ladder.isVisible) {
				Camera.turnTo(ladder)
			}
			val climbPattern = Regex.getPatternForContainsString("Climb")
			val plane = Players.getLocal()?.position?.plane ?: return false
			hoverAndDI(ladder, climbPattern)
			return Execution.delayUntil(
				{ Players.getLocal()?.position?.plane != plane },
				{ Players.getLocal()?.isIdle == false },
				1600
			)
		}

		fun openDoor(nextObject: LocatableEntity): Boolean {
			val house = nextObject.position?.let { getHouseByCoordinate(it) }
				?: return false.also { log.error("No house found") }
			val door = GameObjects.newQuery()
				.names("Door")
				.actions("Open")
				.filter {
					house.area.contains(
						it,
						true
					) && it.area?.surroundingCoordinates?.any { coord -> coord.isReachable } == true
				}
				.results()
				.minByOrNull { it.distanceTo(nextObject) } ?: return false
			log.debug("Opening door at ${door.position} in area ${house.area}")
			hoverAndDI(door, "Open")

			return Execution.delayUntil({ !door.isValid }, { Players.getLocal()?.isIdle == false }, 1600)
		}
	}
}