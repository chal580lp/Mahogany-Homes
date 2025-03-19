package com.recursive.mahoganyhomes

import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.home.FurnitureSlot
import com.recursive.mahoganyhomes.home.MahoganyHome
import com.recursive.mahoganyhomes.home.MaterialType
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.script.framework.listeners.VarbitListener
import com.runemate.game.api.script.framework.listeners.events.VarbitEvent

class FurnitureTracker(private val bot: Bot): VarbitListener {
    private val log = getLogger("FurnitureTracker")

    private val inProgressObjects = mutableSetOf<Int>()
    private val completedObjects = mutableSetOf<Int>()
    private var planksUsed = 0
    private var steelBarsUsed = 0
    private var inventoryPlanks = 0

    fun processVarbActions(mahoganyHome: MahoganyHome, neededActions: List<Triple<Int, Int, String>>) {
        val currentInProgress = neededActions.map { it.first }.toSet()

        val completedObjects = inProgressObjects.filter { it !in currentInProgress }

        completedObjects.forEach { objectId ->
            val furnitureObject = mahoganyHome.houseObjects.objects.find { it.objectId == objectId }
            when (furnitureObject?.type?.material) {
                MaterialType.PLANK -> {
                    planksUsed += furnitureObject.type.materialAmount
                    log.debug("Object $objectId completed, used ${furnitureObject.type.materialAmount} planks. Total: $planksUsed")
                    deductFromPlankSack(furnitureObject.type.materialAmount)
                }
                MaterialType.STEEL_BAR -> {
                    steelBarsUsed += furnitureObject.type.materialAmount
                    log.debug("Object $objectId completed, used ${furnitureObject.type.materialAmount} steel bars. Total: $steelBarsUsed")
                }
                null -> log.warn("Completed object $objectId has no material type")
            }
        }

        inProgressObjects.clear()
        inProgressObjects.addAll(currentInProgress)
    }

    private fun processVarbit(event: VarbitEvent) {
        val slot = FurnitureSlot.entries.find { it.varb == event.varbit.id } ?: return

        val house = bot.contractStateTracker.currentMahoganyHome ?: return
        val objectIndex = slot.varb - FurnitureSlot.SLOT_1.varb

        val furnitureObject = house.houseObjects.objects.getOrNull(objectIndex) ?: return
//        log.debug("Object ${furnitureObject.objectId} changed from ${event.oldValue} to ${event.newValue}")
        when {
            // Repair completed (1 -> 2)
            (event.oldValue == 1 && event.newValue == 2) -> {
                if (furnitureObject.objectId !in completedObjects) {
                    handleCompletion(furnitureObject.objectId, furnitureObject.type.material, furnitureObject.type.materialAmount)
                }
            }
            // Build started (4 -> 7)
            (event.oldValue == 4 && event.newValue in 6..7) -> {
                if (furnitureObject.objectId !in completedObjects) {
                    handleCompletion(furnitureObject.objectId, furnitureObject.type.material, furnitureObject.type.materialAmount)
                    log.debug("Build completed for object ${furnitureObject.objectId}")
                }
            }
            // Remove completed (3 -> 4)
            (event.oldValue == 3 && event.newValue == 4) -> {
                // Don't count materials for removal, just remove from in-progress
                inProgressObjects.remove(furnitureObject.objectId)
                log.debug("Removed object ${furnitureObject.objectId}")
            }
            // Started working on object
            (event.newValue in listOf(1, 3, 4) && furnitureObject.objectId !in inProgressObjects) -> {
                inProgressObjects.add(furnitureObject.objectId)
                log.debug("Started working on object ${furnitureObject.objectId}")
            }
            // Object needs work again or scene change
            (event.newValue in listOf(0, 1, 3, 4)) -> {
                completedObjects.remove(furnitureObject.objectId)
            }
            // Object state reset
            (event.oldValue == 7 && event.newValue == 0) -> {
            }
//            else -> log.debug("Unhandled object ${furnitureObject.objectId} from ${event.oldValue} to ${event.newValue}")
        }
    }

    private fun handleCompletion(objectId: Int, material: MaterialType, amount: Int) {
        when (material) {
            MaterialType.PLANK -> {
                planksUsed += amount
                log.debug("Object $objectId completed, used $amount planks. Total used: $planksUsed")
                deductFromPlankSack(amount)
            }
            MaterialType.STEEL_BAR -> {
                steelBarsUsed += amount
                log.debug("Object $objectId completed, used $amount steel bars. Total used: $steelBarsUsed")
            }
        }
        inProgressObjects.remove(objectId)
    }

    private fun deductFromPlankSack(amount: Int) {
        val currentInvPlanks = Inventory.getQuantity("Plank", "Oak plank", "Teak plank", "Mahogany plank")

        if (currentInvPlanks > inventoryPlanks) {
            inventoryPlanks = currentInvPlanks
            return
        }

        val planksDifference = inventoryPlanks - currentInvPlanks
        if (planksDifference >= 0) {
            if (planksDifference == amount) {
                log.info("Used $amount planks from inventory $currentInvPlanks left")
                inventoryPlanks = currentInvPlanks
                return
            } else if (planksDifference < amount) {
                val remainingToDeduct = amount - planksDifference
                log.info("Used $planksDifference planks from inventory and $remainingToDeduct from sack")
                inventoryPlanks = currentInvPlanks

                val tier = bot.contractStateTracker.contractTier
                if (bot.plankSack.getTierPlankCount(tier) > 0) {
                    bot.plankSack.deductPlanks(remainingToDeduct, tier)
                }
                return
            }
        }

        val tier = bot.contractStateTracker.contractTier
        if (bot.plankSack.getTierPlankCount(tier) > 0) {
            bot.plankSack.deductPlanks(amount, tier)
            log.info("Used $amount planks from sack total left: ${bot.plankSack.getTierPlankCount(tier)}")
        }

        inventoryPlanks = currentInvPlanks
    }

    fun getPlanksUsed(): Int = planksUsed

    fun getSteelBarsUsed(): Int = steelBarsUsed

    fun reset() {
        planksUsed = 0
        steelBarsUsed = 0
        inProgressObjects.clear()
    }

    override fun onValueChanged(event: VarbitEvent?) {
        event ?: return
        processVarbit(event)
    }
}