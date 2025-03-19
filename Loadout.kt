package com.recursive.mahoganyhomes

import com.recursive.mahoganyhomes.common.CommonUtil.sendClientUIWarning
import com.recursive.mahoganyhomes.common.fx.itemdisplay.ItemOptionGroup
import com.recursive.mahoganyhomes.common.fx.itemdisplay.ItemOptionImpl
import com.recursive.mahoganyhomes.common.getLogger
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory

class Loadout(private val bot: Bot) {
	private val log = getLogger("Loadout")

	private fun containedByInventoryOrEquipment(itemOptionGroup: ItemOptionGroup): Boolean {
		return itemOptionGroup.options.any { item -> Inventory.contains(item.id) || Equipment.contains(item.id) }
	}


	fun hasBuildItems(): Boolean {
		if (!containedByInventoryOrEquipment(ItemOptionImpl.hammers) || !containedByInventoryOrEquipment(ItemOptionImpl.saws))  {
			sendClientUIWarning("No identified Hammer or Saw in inventory or equipment")
			return false
		} else {
			log.info("Player has all required items")
			return true
		}
	}
}