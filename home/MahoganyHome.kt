package com.recursive.mahoganyhomes.home

import com.google.common.collect.ImmutableSet
import com.recursive.mahoganyhomes.game.RequiredMaterials
import com.recursive.mahoganyhomes.game.RequiredMaterialsByTier
import com.recursive.mahoganyhomes.home.Houses.ALL_HOUSES
import com.runemate.game.api.hybrid.entities.details.Locatable
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate

data class MahoganyHome(
	val owner: String,
	val location: Coordinate,
	val area: Area,
	val hint: String,
	val npcId: Int,
	val isUpstairs: Boolean = false,
	val houseObjects: HouseObjects,
	val requiredMaterials: RequiredMaterialsByTier,
	val ladders: Set<Int> = emptySet(),
) {
	fun getName(): String = owner

	fun getRequiredPlanks(tier: Int): Int {
	val materials = getRequiredMaterials(tier) ?: return -2
	if (materials.minPlanks + materials.maxPlanks == 0) return -3

	return materials.maxPlanks
}

	fun getRequiredSteelBars(tier: Int): Int {
		val materials = getRequiredMaterials(tier) ?: return -2
		if (materials.minSteelBars + materials.maxSteelBars == 0) return -3

		return materials.maxSteelBars
	}

	private fun getRequiredMaterials(tier: Int): RequiredMaterials? {
		return requiredMaterials.getByTier(tier)
	}

	companion object {
		val ALL_LADDERS: ImmutableSet<Int> by lazy {
			Houses.ALL_HOUSES
				.flatMap { it.ladders }
				.toSet()
				.let { ImmutableSet.copyOf(it) }
		}

		fun isLadder(objId: Int): Boolean = ALL_LADDERS.contains(objId)

		// function to get a house? if area contains a coordinate
		fun getHouseByCoordinate(locatable: Locatable): MahoganyHome? {
			return ALL_HOUSES.firstOrNull { it.area.contains(locatable, true) }
		}
	}
}