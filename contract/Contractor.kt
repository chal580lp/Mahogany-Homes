package com.recursive.mahoganyhomes.contract

import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.hybrid.region.Players

enum class Contractor(val gameName: String, val city: String, val location: Coordinate) {
	ELLIE("Ellie", "Ardougne", Coordinate(2638, 3294, 0)),
	AMY("Amy", "Falador", Coordinate(2989, 3365, 0)),
	ANGELO("Angelo", "Hosidius", Coordinate(1781, 3627, 0)),
	MARLO("Marlo", "Varrock", Coordinate(3240, 3472, 0));

	companion object {
		fun closestToPlayer(): Contractor {
			return entries.toTypedArray().minByOrNull { it.location.distanceTo(Players.getLocal()) }!!
		}

		fun names(): Array<String> {
			return entries.map { it.gameName }.toTypedArray()
		}
	}
}