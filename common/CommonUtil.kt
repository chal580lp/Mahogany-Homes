package com.recursive.mahoganyhomes.common

import com.runemate.game.api.client.ClientUI

object CommonUtil {
	private val log = getLogger("CommonUtil")

	fun sendClientUIWarning(message: String) {
		ClientUI.showAlert(ClientUI.AlertLevel.WARN, message)
	}
}