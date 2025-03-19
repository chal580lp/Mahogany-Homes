package com.recursive.mahoganyhomes

import com.recursive.mahoganyhomes.common.getLogger
import com.runemate.game.api.hybrid.location.Coordinate

object BankLocation {
	private val log = getLogger("BankStop")

	val HOSIDIUS = Coordinate(1750,3598,0)
	val ARDOUGNE_SOUTH = Coordinate(2653,3284,0)
	val FALADOR_EAST = Coordinate(3012,3356,0)
	val VARROCK_EAST = Coordinate(3253,3420,0)

	val BANKS = listOf(HOSIDIUS, ARDOUGNE_SOUTH, FALADOR_EAST, VARROCK_EAST)

	fun getClosestTo(coord: Coordinate?): Coordinate? {
		return BANKS.minByOrNull { it.distanceTo(coord) }
	}
}