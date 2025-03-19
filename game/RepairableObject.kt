package com.recursive.mahoganyhomes.game

import com.recursive.mahoganyhomes.home.FurnitureType
import com.runemate.game.api.hybrid.entities.GameObject

data class RepairableObject(
	val gameObject: GameObject,
	val type: FurnitureType,
	val action: String  // "repair", "remove", "build", "climb"
)
