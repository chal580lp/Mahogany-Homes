package com.recursive.mahoganyhomes.item

import com.recursive.mahoganyhomes.common.getLogger
import com.runemate.game.api.client.ClientUI
import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.listeners.ChatboxListener
import com.runemate.game.api.script.framework.listeners.events.MessageEvent

class PlankSack : ChatboxListener {
    private val log = getLogger("PlankSack")

    var hasChecked = false
    private var hasCheckedCount = 0
    private var regularPlanks = 0
    private var oakPlanks = 0
    private var teakPlanks = 0
    private var mahoganyPlanks = 0

    // Keep track of inventory quantities before filling
    private var previousRegular = 0
    private var previousOak = 0
    private var previousTeak = 0
    private var previousMahogany = 0

    companion object {
        private val PLANK_SACK_PATTERN = Regex("Basic planks: (\\d+), Oak planks: (\\d+), Teak planks: (\\d+), Mahogany planks: (\\d+)")
        private val FULL_SACK_PATTERN = Regex("Your sack is full.")
        private val EMPTY_SACK_PATTERN = Regex("Your sack is empty.")
        private const val MAX_TOTAL_PLANKS = 28
    }

    data class PlankQuantities(
        val regular: Int = 0,
        val oak: Int = 0,
        val teak: Int = 0,
        val mahogany: Int = 0
    ) {
        val total: Int get() = regular + oak + teak + mahogany
    }

    override fun onMessageReceived(event: MessageEvent?) {
        if (event == null) return
        if (event.type != Chatbox.Message.Type.SERVER && event.type != Chatbox.Message.Type.UNKNOWN) return
        val msg = event.message

        PLANK_SACK_PATTERN.find(msg)?.let { match ->
            regularPlanks = match.groupValues[1].toInt()
            oakPlanks = match.groupValues[2].toInt()
            teakPlanks = match.groupValues[3].toInt()
            mahoganyPlanks = match.groupValues[4].toInt()
            hasChecked = true
            hasCheckedCount = 0
            log.info("Updated plank sack contents: Regular=$regularPlanks, Oak=$oakPlanks, Teak=$teakPlanks, Mahogany=$mahoganyPlanks")
        }
        FULL_SACK_PATTERN.find(msg)?.let {
            log.info("Plank sack is full, setting hasChecked to false")
            hasChecked = false
            hasCheckedCount = 0
        }
        EMPTY_SACK_PATTERN.find(msg)?.let {
            log.info("Plank sack is empty, setting hasChecked to false")
            hasChecked = false
            hasCheckedCount = 0
            regularPlanks = 0
            oakPlanks = 0
            teakPlanks = 0
            mahoganyPlanks = 0
        }

    }

    fun fill() {
        log.info("Filling plank sack")
        if (!storePreviousQuantities()) {
            log.warn("No planks to fill")
            return
        }
        val emptySlots = Inventory.getEmptySlots()
        val sack = Inventory.getItems("Plank sack").first() ?: return
        if (Bank.isOpen()) {
            sack.interact("Use")
        } else {
            sack.interact("Fill")
        }
        if (!Execution.delayUntil({ Inventory.getEmptySlots() != emptySlots}, 2000)) {
            log.warn("Failed to fill plank sack")
            return
        }
        val addedPlanks = calculateAddedPlanks()

        regularPlanks += addedPlanks.regular
        oakPlanks += addedPlanks.oak
        teakPlanks += addedPlanks.teak
        mahoganyPlanks += addedPlanks.mahogany
        log.info("Total planks ${getTotalPlanks()}")
    }

    fun empty() {
        storePreviousQuantities()
        val sack = Inventory.getItems("Plank sack").first() ?: return
        val emptySlots = Inventory.getEmptySlots()
        if (Bank.isOpen()) {
            sack.interact("Use")
        } else {
            sack.interact("Empty")
        }
        if (!Execution.delayUntil({ Inventory.getEmptySlots() != emptySlots}, 2000)) {
            log.warn("Failed to empty plank sack")
            return
        }

        val removedPlanks = calculateRemovedPlanks()
        regularPlanks -= removedPlanks.regular
        oakPlanks -= removedPlanks.oak
        teakPlanks -= removedPlanks.teak
        mahoganyPlanks -= removedPlanks.mahogany
        log.info("Total planks ${getTotalPlanks()}")

    }

    private fun storePreviousQuantities(): Boolean {
        previousRegular = Inventory.getQuantity("Plank")
        previousOak = Inventory.getQuantity("Oak plank")
        previousTeak = Inventory.getQuantity("Teak plank")
        previousMahogany = Inventory.getQuantity("Mahogany plank")
        return previousRegular + previousOak + previousTeak + previousMahogany > 0
    }

    private fun calculateAddedPlanks(): PlankQuantities {
        val currentRegular = Inventory.getQuantity("Plank")
        val currentOak = Inventory.getQuantity("Oak plank")
        val currentTeak = Inventory.getQuantity("Teak plank")
        val currentMahogany = Inventory.getQuantity("Mahogany plank")

        return PlankQuantities(
            regular = previousRegular - currentRegular,
            oak = previousOak - currentOak,
            teak = previousTeak - currentTeak,
            mahogany = previousMahogany - currentMahogany
        ).also {
            log.info("Added to plank sack: ${if (it.regular > 0) "${it.regular} regular" else ""}${if (it.oak > 0) "${it.oak} oak" else ""}${if (it.teak > 0) "${it.teak} teak" else ""}${if (it.mahogany > 0) "${it.mahogany} mahogany" else ""}")
        }
    }

    private fun calculateRemovedPlanks(): PlankQuantities {
        val currentRegular = Inventory.getQuantity("Plank")
        val currentOak = Inventory.getQuantity("Oak plank")
        val currentTeak = Inventory.getQuantity("Teak plank")
        val currentMahogany = Inventory.getQuantity("Mahogany plank")

        return PlankQuantities(
            regular = currentRegular - previousRegular,
            oak = currentOak - previousOak,
            teak = currentTeak - previousTeak,
            mahogany = currentMahogany - previousMahogany
        ).also {
            log.info("Removed from plank sack: ${if (it.regular > 0) "${it.regular} regular" else ""}${if (it.oak > 0) "${it.oak} oak" else ""}${if (it.teak > 0) "${it.teak} teak" else ""}${if (it.mahogany > 0) "${it.mahogany} mahogany" else ""}")
        }
    }

    fun getCurrentQuantities(): PlankQuantities {
        return PlankQuantities(regularPlanks, oakPlanks, teakPlanks, mahoganyPlanks)
    }

    fun getTotalPlanks(): Int {
        return regularPlanks + oakPlanks + teakPlanks + mahoganyPlanks
    }

    fun hasSpace(): Boolean {
        return getTotalPlanks() < MAX_TOTAL_PLANKS
    }

    fun getRemainingSpace(): Int {
        return MAX_TOTAL_PLANKS - getTotalPlanks()
    }

    fun canAdd(amount: Int): Boolean {
        return getTotalPlanks() + amount <= MAX_TOTAL_PLANKS
    }

    fun isEmpty(): Boolean {
        return getTotalPlanks() == 0
    }

    fun isFull(): Boolean {
        return getTotalPlanks() >= MAX_TOTAL_PLANKS
    }

    fun inInventory(): Boolean {
        return Inventory.contains("Plank sack")
    }

    fun getTierPlankCount(selectedTier: Int): Int {
        return when (selectedTier) {
            1 -> regularPlanks
            2 -> oakPlanks
            3 -> teakPlanks
            4 -> mahoganyPlanks
            else -> 0
        }
    }

    fun checkPlanksInside(): Boolean {
        return if (!hasChecked && inInventory()) {
            if (Bank.isOpen()) Bank.close() else check()
            true
        } else {
            false
        }
    }

    fun check() {
        log.info("Checking plank sack")
        val sack = Inventory.getItems("Plank sack").firstOrNull() ?: return
        if (!sack.interact("Check")) return
        Execution.delayUntil({ hasChecked }, 2000)
        if (!hasChecked) {
            hasCheckedCount++
            log.debug("Checked count: $hasCheckedCount")
            if (hasCheckedCount >= 5) {
                ClientUI.showError("Failed to find Plank Sack message event after 5 checks, This likely means Message Event listener didn't attach. You need to restart RuneLite.")
                Environment.getBot()?.stop("Message Event Listener failed to attach")
            }
        }
    }

    fun deductPlanks(amount: Int, tier: Int) {
        val currentAmount = getTierPlankCount(tier)

        val deductAmount = amount.coerceAtMost(currentAmount)

        if (deductAmount < amount) {
            log.warn("Can only deduct $deductAmount planks of tier $tier (requested: $amount)")
        }

        when (tier) {
            1 -> regularPlanks = (regularPlanks - deductAmount).coerceAtLeast(0)
            2 -> oakPlanks = (oakPlanks - deductAmount).coerceAtLeast(0)
            3 -> teakPlanks = (teakPlanks - deductAmount).coerceAtLeast(0)
            4 -> mahoganyPlanks = (mahoganyPlanks - deductAmount).coerceAtLeast(0)
        }
    }

    fun hasWrongTierPlanks(tier: Int): Boolean {
        val totalPlanks = getTotalPlanks()
        val tierPlanks = getTierPlankCount(tier)
        return totalPlanks != tierPlanks
    }
}