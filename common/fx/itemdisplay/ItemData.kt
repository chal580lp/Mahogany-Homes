package com.recursive.mahoganyhomes.common.fx.itemdisplay

data class ItemData(
	val name: String,
	val id: Int,
	val quantity: Int = 1,
	val alternatives: List<ItemData> = emptyList(),
	val note: String? = null,
	val requirements: List<RequirementData> = emptyList()
)