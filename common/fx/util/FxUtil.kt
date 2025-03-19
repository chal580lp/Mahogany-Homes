package com.recursive.mahoganyhomes.common.fx.util

import com.recursive.mahoganyhomes.common.getLogger
import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.util.Duration
import org.apache.commons.io.output.ByteArrayOutputStream
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO
import java.time.Duration as JavaDuration

object FxUtil {
	private val log = getLogger("FxUtil")
	const val IMAGES_DIRECTORY = "/images/items"

	fun createGradualDecryptionAnimation(label: Label, finalText: String, duration: Duration): Timeline {
		val timeline = Timeline()
		val steps = 30
		val stepDuration = duration.divide(steps.toDouble())
		var currentText = generateRandomText(finalText)

		for (i in 0 until steps) {
			timeline.keyFrames.add(KeyFrame(stepDuration.multiply(i.toDouble()), {
				currentText = decryptTextGradually(currentText, finalText, (i + 1).toDouble() / steps)
				label.text = currentText
			}))
		}

		return timeline
	}

	private fun decryptTextGradually(currentText: String, finalText: String, progress: Double): String {
		val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?"
		return currentText.mapIndexed { index, c ->
			when {
				c == finalText[index] -> c
				Math.random() < progress -> finalText[index]
				Math.random() < 0.3 -> chars.random()
				else -> c
			}
		}.joinToString("")
	}

	private fun generateRandomText(text: String): String {
		val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?"
		return text.map { if (it.isWhitespace()) it else chars.random() }.joinToString("")
	}

	fun getImageForItem(itemId: Int): Image {
		if (itemId == 995) return getImageForItem(1003)
		val basedir = Environment.getSharedStorageDirectory().toString()
		val imageFile = File("$basedir$IMAGES_DIRECTORY/$itemId.png")
		return if (imageFile.exists()) {
			loadImageSafely(imageFile)
		} else {
			try {
				val item = SpriteItem(itemId, 0)
				val bufferedImage = item.image.get() ?: return getImageForItem(1003)
				saveImage(bufferedImage, imageFile)
				createJavaFXImage(bufferedImage)
			} catch (e: Exception) {
				log.error("Error while getting image for item $itemId", e)
				createErrorImage()
			}
		}
	}

	private fun loadImageSafely(file: File): Image {
		return try {
			Image(file.toURI().toString())
		} catch (e: Exception) {
			log.error("Failed to load image from file: ${file.absolutePath}", e)
			createErrorImage()
		}
	}

	private fun saveImage(bufferedImage: BufferedImage, file: File) {
		try {
			file.parentFile.mkdirs() // Ensure the directory exists
			ImageIO.write(bufferedImage, "png", file)
		} catch (e: Exception) {
			log.error("Failed to save image: ${file.absolutePath}", e)
		}
	}

	private fun createJavaFXImage(bufferedImage: BufferedImage): Image {
		val baos = ByteArrayOutputStream()
		ImageIO.write(bufferedImage, "png", baos)
		return Image(ByteArrayInputStream(baos.toByteArray()))
	}

	private fun createErrorImage(): Image {
		val width = 36
		val height = 36
		val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		val g = bufferedImage.createGraphics()
		g.color = java.awt.Color.BLACK
		g.fillRect(0, 0, width, height)
		g.color = java.awt.Color.RED
		g.drawString("No Img", 2, 12)
		g.dispose()
		return createJavaFXImage(bufferedImage)
	}

	fun getImageForItemSafe(itemId: Int): CompletableFuture<Image> {
		if (itemId == 995) return getImageForItemSafe(1003)

		val basedir = Environment.getSharedStorageDirectory().toString()
		val imageFile = File("$basedir$IMAGES_DIRECTORY/$itemId.png")

		return if (imageFile.exists()) {
			CompletableFuture.supplyAsync { loadImageSafely(imageFile) }
		} else {
			val future = CompletableFuture<Image>()
			Environment.getBot()?.platform?.submit {
				val image = getImageForItem(itemId)
				future.complete(image)
			}
			future
		}
	}

	fun formatDuration(duration: JavaDuration?): String {
		if (duration == null) return "0m 0s"
		val minutes = duration.toMinutes()
		val seconds = duration.minusMinutes(minutes).seconds
		return "${minutes}m ${seconds}s"
	}
}