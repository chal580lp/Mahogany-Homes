package com.recursive.mahoganyhomes

import com.recursive.mahoganyhomes.common.getLogger
import com.recursive.mahoganyhomes.contract.ContractCompleter
import com.recursive.mahoganyhomes.contract.ContractFetcher
import com.recursive.mahoganyhomes.contract.ContractStateTracker
import com.recursive.mahoganyhomes.fx.StatsPanel
import com.recursive.mahoganyhomes.fx.scenebuilder.SettingsController
import com.recursive.mahoganyhomes.home.MahoganyHome
import com.recursive.mahoganyhomes.home.MahoganyHome.Companion.getHouseByCoordinate
import com.recursive.mahoganyhomes.item.PlankSack
import com.runemate.game.api.client.ClientUI
import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution
import com.runemate.game.api.script.framework.LoopingBot
import com.runemate.game.api.script.framework.listeners.ChatboxListener
import com.runemate.game.api.script.framework.listeners.EngineListener
import com.runemate.game.api.script.framework.listeners.events.EngineEvent
import com.runemate.game.api.script.framework.listeners.events.MessageEvent
import com.runemate.pathfinder.Pathfinder
import com.runemate.ui.DefaultUI
import javafx.application.Platform
import javafx.scene.layout.VBox
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class Bot : LoopingBot(), EngineListener,  ChatboxListener  {
	private val log = getLogger("Bot")
	val plankSack = PlankSack()
	private val furnitureTracker = FurnitureTracker(this)
	private val furnitureFinder = FurnitureFinder(furnitureTracker)
	val contractStateTracker = ContractStateTracker(this)
	private val contractCompleter = ContractCompleter()
	private val contractFetcher = ContractFetcher(this)
	private val statsPanel = StatsPanel(this, contractStateTracker)
	val loadout = Loadout(this)

	private lateinit var pathfinder: Pathfinder
	private lateinit var navigationManager: NavigationManager
	private lateinit var supplyManager: SupplyManager
	private lateinit var repairManager: RepairManager

	private var status = ""
	var selectedContractTier: Int = 0
	var hosidiusTeleportMethod: String = "Runes"
	var isFaladorOnComplete = false
	var stopBeforeNextHour = false
	var closestBankDistance = 0
	var atHouse: MahoganyHome? = null

	override fun onStart(vararg arguments: String?) {
		log.info("onStart")
		DefaultUI.addPanel(0,this,"Settings", VBox(SettingsController(this), statsPanel),true)
		eventDispatcher.addListener(this)
		eventDispatcher.addListener(contractStateTracker)
		eventDispatcher.addListener(plankSack)
		eventDispatcher.addListener(furnitureTracker)

		pathfinder = Pathfinder.create(this)
		navigationManager = NavigationManager(this, pathfinder)
		supplyManager = SupplyManager(this, contractStateTracker, plankSack, navigationManager)
		repairManager = RepairManager(furnitureFinder)

	}

	override fun onLoop() {
		if (checkStopBeforeNextHour()) return
		if (!statsPanel.isRunning()) return
		if (plankSack.checkPlanksInside()) return
		if (supplyManager.fillPlankSack()) return
		if (supplyManager.emptyPlankSack()) return
		if (supplyManager.handleBanking()) return

		if (contractStateTracker.needsNewContract()) {
			log.info("Getting new contract")
			contractFetcher.getNewContract(selectedContractTier)
			return
		}
		val player = Players.getLocal() ?: return
		contractStateTracker.currentMahoganyHome?.let { house ->
			if (!house.area.contains(player, true)) {
				updateStatus("Navigating to ${house.owner} house")
				navigationManager.navigateToHouse(house)
				return
			}
			if (contractStateTracker.hasFinishedCurrentHouse()) {
				updateStatus("Handing in contract")
				contractCompleter.complete(house)
			} else {
				if (isBuildInterfaceOpen()) {
					log.warn("Furniture Creation Menu is open, setting needToBank to true")
					supplyManager.needToBank = true
					return
				}
				repairManager.repairHome(house)
			}
		} ?: run {
			log.debug("No house assigned continuing dialog")
			ChatDialog.getContinue()?.select()
		}
	}

	override fun onStop(reason: String?) {
		eventDispatcher.removeListener(contractStateTracker)
		log.info("Stopping bot")
	}

	private fun checkStopBeforeNextHour():Boolean {
		if (!stopBeforeNextHour) return false
		val runtime = Environment.getRuntime(this).toDuration(DurationUnit.MILLISECONDS)
		val hour = runtime.inWholeHours
		val minutes = runtime.inWholeMinutes % 60
		val seconds = runtime.inWholeSeconds % 60
		if (minutes in 57..59) {
			log.info("Stopping bot as Stop Before Next Hour is enabled. Runtime: $hour:$minutes:$seconds")
			ClientUI.showAlert(ClientUI.AlertLevel.INFO,"Stopping bot as Stop Before Next Hour is enabled. Runtime: $hour:$minutes:$seconds")
			stop("Stop Before Next Hour")
			return true
		}
		return false
	}

	fun navigateTo(destination: Coordinate) {
		navigationManager.navigateTo(destination)
	}

	private fun isBuildInterfaceOpen(): Boolean {
		val buildInterface = Interfaces.newQuery().types(InterfaceComponent.Type.LABEL).textContains("Furniture Creation Menu").containers(458).results().firstOrNull()
		return buildInterface != null && buildInterface.isVisible
	}

	fun updateStatus(text: String) {
		if (status != text) {
			status = text
			log.info("Status: $text")
			DefaultUI.setStatus(text)
		}
	}

	fun stopAndPause(reason: String) {
		log.error(reason)
		ClientUI.showAlert(ClientUI.AlertLevel.WARN, "Bot paused: $reason. Will stop if not resumed in 5 minutes.")
		pause(reason)
		if (!Execution.delayUntil({ !isPaused }, 300000)) {
			this.platform.submit {
				stop(reason)
			}
		}
	}

	override fun onEngineEvent(event: EngineEvent?) {
		if (event?.type == EngineEvent.Type.SERVER_TICK) {
			val player = Players.getLocal() ?: return
			val position = Coordinate(player.position?.x ?: 0, player.position?.y ?: 0, 0)
			atHouse = player.position?.let { getHouseByCoordinate(it) }
			val distanceToAssignedHouse = contractStateTracker.currentMahoganyHome?.location?.distanceTo(position)?.toInt() ?: -1
			closestBankDistance = BankLocation.getClosestTo(position)?.distanceTo(position)?.toInt() ?: -1
			Platform.runLater {
				statsPanel.updateAtHouse(atHouse)
				statsPanel.updateDistanceToHouse(distanceToAssignedHouse)
				statsPanel.updateContractsCompleted(contractStateTracker)
				statsPanel.updateRequiredMaterials(contractStateTracker.currentMahoganyHome)
				statsPanel.updatePlankSack(plankSack)
				statsPanel.updateBankDistance(closestBankDistance)
				statsPanel.updateFurnitureBuilt(furnitureTracker)
			}
		}
	}


	override fun onPause() {
		log.debug("Paused")
	}

	override fun onMessageReceived(p0: MessageEvent?) {
		if (p0?.type != Chatbox.Message.Type.SERVER && p0?.type != Chatbox.Message.Type.UNKNOWN)  return
		val message = p0.message ?: return

		if (message.contains("Runes to cast this spell")) {
			stopAndPause("No Runes left to cast teleports")
		}
	}
}