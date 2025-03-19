package com.recursive.mahoganyhomes.common.fx.itemdisplay

import com.recursive.mahoganyhomes.Bot
import com.recursive.mahoganyhomes.common.fx.util.FxUtil.getImageForItemSafe
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Popup
import javafx.util.Duration

class ItemDisplayPanel(private val bot: Bot) : StackPane() {
	private val itemContainer = HBox()
	private val popup = Popup()
	private val infoPanel = VBox().apply {
		style = """
            -fx-background-color: #2B2B2B;
            -fx-border-color: #565656;
            -fx-border-width: 1;
            -fx-border-radius: 4;
            -fx-background-radius: 4;
            -fx-padding: 8;
            -fx-spacing: 4;
        """
	}

	init {
		itemContainer.apply {
			spacing = 4.0
			padding = Insets(10.0)
			style = """
                -fx-background-color: #2B2B2B;
                -fx-background-radius: 5;
            """
			alignment = Pos.CENTER_LEFT
		}

		popup.content.add(infoPanel)
		children.add(itemContainer)
	}

	fun displayItems(itemRequirement: ItemRequirement) {
		itemContainer.children.clear()
		itemRequirement.groups.sortedBy { it.options.first().quantity }.forEach { group ->
			val itemDisplay = ItemOptionDisplay(bot, group, popup, infoPanel, this)
			itemContainer.children.add(itemDisplay)
			// Load images after UI setup
			Platform.runLater { itemDisplay.loadImages() }
		}
	}

	private class ItemOptionDisplay(
		private val bot: Bot,
		private val group: ItemOptionGroup,
		private val popup: Popup,
		private val infoPanel: VBox,
		private val parent: ItemDisplayPanel,
	) : StackPane() {
		private var currentIndex = 0
		private val imageView = ImageView()
		private val quantityLabel = Label()
		private val timeline: Timeline
		private val hoverEffect = ColorAdjust().apply {
			brightness = 0.2
			saturation = 0.1
		}


		init {
			minWidth = 38.0
			minHeight = 38.0
			maxWidth = 38.0
			maxHeight = 38.0

			setupBaseUI()
			setupInteractivity()

			timeline = if (group.options.size > 1) {
				Timeline(
					KeyFrame(
						Duration.seconds(2.0),
						javafx.event.EventHandler {
							currentIndex = (currentIndex + 1) % group.options.size
							updateDisplay(false) // Don't reload images on timeline update
						}
					)
				).apply {
					cycleCount = 1
					play()
				}
			} else {
				Timeline()
			}

			updateDisplay(false)
		}

		private fun setupBaseUI() {
			// Move UI setup code here from init
			var baseStyle = """
                -fx-background-color: #464646;
                -fx-border-color: #565656;
                -fx-border-width: 1;
                -fx-border-radius: 2;
                -fx-background-radius: 2;
            """

			if (group.options.size > 1) {
				baseStyle += """
                    -fx-border-color: #707070;
                    -fx-border-width: 2;
                """
			}

			style = baseStyle

			imageView.apply {
				fitWidth = 36.0
				fitHeight = 36.0
				isPreserveRatio = true
				isSmooth = true
			}

			quantityLabel.apply {
				style = """
                    -fx-text-fill: white;
                    -fx-font-size: 10px;
                    -fx-background-color: rgba(0, 0, 0, 0.7);
                    -fx-padding: 1 3;
                    -fx-background-radius: 2;
                """
				StackPane.setAlignment(this, Pos.BOTTOM_RIGHT)
				StackPane.setMargin(this, Insets(0.0, 1.0, 1.0, 0.0))
			}

			children.addAll(imageView, quantityLabel)
		}

		fun loadImages() {
			val currentItem = group.options[currentIndex]
			try {
				bot.platform.submit {
					getImageForItemSafe(currentItem.id).thenAcceptAsync({ image ->
						imageView.image = image
					}, Platform::runLater)
				}
			} catch (e: Exception) {
				try {
					val spriteImage = createSpriteImageView(currentItem.id).image
					imageView.image = spriteImage
				} catch (e: Exception) {
					println("Failed to load image for item: ${currentItem.name} (ID: ${currentItem.id})")
					e.printStackTrace()
				}
			}
		}

		private fun updateDisplay(loadImages: Boolean = true) {
			val currentItem = group.options[currentIndex]

			if (loadImages) {
				loadImages()
			}

			if (currentItem.quantity > 1) {
				quantityLabel.text = currentItem.quantity.toString()
				quantityLabel.isVisible = true
			} else {
				quantityLabel.isVisible = false
			}
		}

		private fun setupInteractivity() {
			cursor = Cursor.HAND

			setOnMouseEntered {
				imageView.effect = hoverEffect
				timeline.pause()
				updateInfoPanel()
				showPopup(it.screenX, it.screenY)
			}

			setOnMouseMoved {
				if (popup.isShowing) {
					showPopup(it.screenX, it.screenY)
				}
			}

			setOnMouseExited {
				imageView.effect = null
				if (group.options.size > 1) {
					timeline.play()
				}
				popup.hide()
			}

			setOnMouseClicked {
				if (group.options.size > 1) {
					currentIndex = (currentIndex + 1) % group.options.size
					updateDisplay()
					if (popup.isShowing) {
						updateInfoPanel()
					}
				}
			}
		}

		private fun showPopup(x: Double, y: Double) {
			val currentWindow = scene?.window ?: return

			popup.x = x + 15
			popup.y = y - 10

			val currentScene = currentWindow.scene as Scene
			val windowWidth = currentScene.width
			val windowHeight = currentScene.height
			val windowX = currentWindow.x
			val windowY = currentWindow.y

			if (popup.x + popup.width > windowX + windowWidth) {
				popup.x = windowX + windowWidth - popup.width - 10
			}

			if (popup.y + popup.height > windowY + windowHeight) {
				popup.y = windowY + windowHeight - popup.height - 10
			}

			popup.show(currentWindow)
		}

		private fun updateInfoPanel() {
			val currentItem = group.options[currentIndex]
			infoPanel.children.clear()
			infoPanel.children.add(Label(currentItem.name).apply {
				style = "-fx-text-fill: #ffd700; -fx-font-weight: bold; -fx-font-size: 12px;"
			})

			group.description?.let {
				infoPanel.children.add(Label("${group.description}").apply {
					style = "-fx-text-fill: #99ff99; -fx-font-size: 11px;"
				})
			}

			if (currentItem.quantity > 1) {
				infoPanel.children.add(Label("Quantity: ${currentItem.quantity}").apply {
					style = "-fx-text-fill: white; -fx-font-size: 11px;"
				})
			}

			if (group.options.size > 1) {
				infoPanel.children.add(Label("Option ${currentIndex + 1} of ${group.options.size}").apply {
					style = "-fx-text-fill: #a0a0a0; -fx-font-size: 11px;"
				})
			}

			currentItem.requirements.forEach { req ->
				infoPanel.children.add(Label("$req").apply {
					style = "-fx-text-fill: #ff9999; -fx-font-size: 11px;"
				})
			}

			group.usedFor.forEach { usedFor ->
				infoPanel.children.add(Label(usedFor.toString()).apply {
					style = "-fx-text-fill: #99ccff; -fx-font-size: 11px;"
				})
			}
		}

		private fun updateDisplay() {
			val currentItem = group.options[currentIndex]
			try {
				bot.platform.submit {
					getImageForItemSafe(currentItem.id).thenAcceptAsync({ image ->
						imageView.image = image
					}, Platform::runLater)
				}
			} catch (e: Exception) {
				try {
					val spriteImage = createSpriteImageView(currentItem.id).image
					imageView.image = spriteImage
				} catch (e: Exception) {
					println("Failed to load image for item: ${currentItem.name} (ID: ${currentItem.id})")
					e.printStackTrace()
				}
			}

			if (currentItem.quantity > 1) {
				quantityLabel.text = currentItem.quantity.toString()
				quantityLabel.isVisible = true
			} else {
				quantityLabel.isVisible = false
			}
		}

		private fun createSpriteImageView(id: Int): ImageView {
			return ImageView().apply {
				fitHeight = 36.0
				fitWidth = 36.0
				val item = SpriteItem(id, 0)
				val image = item.image.get()
				this.image = SwingFXUtils.toFXImage(image, null)
			}
		}
	}
}