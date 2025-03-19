package com.recursive.mahoganyhomes.common.fx.itemdisplay

object ItemOptionImpl {

	val hammers = ItemOptionGroup.fromList(
		"Hammer",
		ItemDataImpl.ALL_HAMMERS,
		UsedFor.MAHOGANY_HOMES
	)
	val saws = ItemOptionGroup.fromList(
		"Saw",
		ItemDataImpl.ALL_SAWS,
		UsedFor.MAHOGANY_HOMES
	)

}