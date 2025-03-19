package com.recursive.mahoganyhomes

import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.common.hoverAndDI
import com.recursive.mahoganyhomes.game.RepairableObject
import com.recursive.mahoganyhomes.home.Houses
import com.recursive.mahoganyhomes.home.MahoganyHome
import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.entities.Player
import com.runemate.game.api.hybrid.local.Camera
import com.runemate.game.api.hybrid.local.MouseCamera
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.hybrid.util.Regex
import com.runemate.game.api.osrs.local.hud.interfaces.ControlPanelTab
import com.runemate.game.api.script.Execution
import com.runemate.ui.DefaultUI

class RepairManager(
    private val furnitureFinder: FurnitureFinder,
) {
    private val log = getLogger("RepairManager")

    fun repairHome(currentMahoganyHome: MahoganyHome?) {
        val nextObject = currentMahoganyHome?.let { furnitureFinder.getNextRepairableObject(it) } ?: return
        val gameObject = nextObject.gameObject
        val objectName = gameObject.activeDefinition?.name ?: "NULL NAME".also { log.error("Object name is null") }

        if (!isObjectReachable(gameObject, currentMahoganyHome)) return
        if (!gameObject.isVisible) MouseCamera.turnTo(gameObject)
        if (!ControlPanelTab.INVENTORY.open()) return
        if (Camera.getZoom() > 0.15) Camera.setZoom(0.1,0.1)
        handleObjectInteraction(nextObject, gameObject, objectName)
    }

    private fun isObjectReachable(gameObject: GameObject, currentMahoganyHome: MahoganyHome?): Boolean {
        if (gameObject.area?.coordinates?.any { it.isReachable } != false) return true

        log.debug("Object {} surrounding is not reachable", gameObject)

        val player = Players.getLocal() ?: return false
        if (currentMahoganyHome == Houses.NOELLA && player.position?.plane == 1) {
            log.info("Climbing down Noella's ladder")
            FurnitureFinder.climbLadder(Houses.NOELLA)
            return false
        }

        if (FurnitureFinder.openDoor(gameObject)) return false

        log.warn("No door in House area, continuing with interaction")
        return true
    }

    private fun handleObjectInteraction(nextObject: RepairableObject, gameObject: GameObject, objectName: String) {
        val player = Players.getLocal() ?: return
        val interactAction = when (nextObject.action) {
            "climb" -> {
                DefaultUI.setStatus("Climbing $objectName")
                handleClimbing(gameObject, objectName, player)
                return
            }

            "repair" -> {
                DefaultUI.setStatus("Repairing $objectName")
                log.debug(
                    "Repairing {} with {} ({})",
                    objectName,
                    nextObject.type.material,
                    nextObject.type.materialAmount
                )
                "Repair"
            }

            "remove" -> {
                DefaultUI.setStatus("Removing $objectName")
                log.info("Removing: $objectName")
                "Remove"
            }

            "build" -> {
                DefaultUI.setStatus("Building $objectName")
                log.info("Building: $objectName")
                "Build"
            }

            else -> {
                log.error("$nextObject")
                log.error("Unknown action: ${nextObject.action}")
                return
            }
        }

        performInteraction(gameObject, interactAction, player)

    }

    private fun handleClimbing(gameObject: GameObject, objectName: String, player: Player) {
        log.debug("Climbing $objectName")
        val plane = player.position?.plane ?: return
        val climbPattern = Regex.getPatternForContainsString("Climb")

        hoverAndDI(gameObject, climbPattern)

        Execution.delayUntil(
            { player.position?.plane != plane },
            { !player.isIdle },
            1600
        )

    }

    private fun performInteraction(gameObject: GameObject, action: String, player: Player) {
        hoverAndDI(gameObject, action)
        Execution.delay(600)
        Execution.delayUntil(
            { !player.isMoving },
            { player.isMoving }, 600, 1800
        )
        if (action == "Build") {
            Execution.delay(600)
        } else if (action == "Remove") {
            Execution.delay(300)
        }
    }
}