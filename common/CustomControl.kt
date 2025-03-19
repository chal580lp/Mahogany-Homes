package com.recursive.mahoganyhomes.common

import com.runemate.game.api.hybrid.util.Resources
import com.runemate.game.api.script.framework.AbstractBot
import javafx.fxml.JavaFXBuilderFactory
import javafx.scene.layout.Pane
import java.io.IOException

interface CustomControl {
	fun loadView(bot: AbstractBot, resource: String) {
		if (this !is Pane) {
			throw IllegalStateException("${this::class.simpleName} is not a Pane!")
		}

		javafx.application.Platform.runLater {
			try {
				val input = Resources.getAsStream(bot, resource)
				val loader = javafx.fxml.FXMLLoader()
				loader.setController(this)
				loader.builderFactory = JavaFXBuilderFactory()
				val root = loader.load<Pane>(input)
				(this as Pane).children.setAll(root)
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
	}
}