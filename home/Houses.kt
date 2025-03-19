package com.recursive.mahoganyhomes.home

import com.recursive.mahoganyhomes.game.RequiredMaterialsByTier
import com.runemate.game.api.hybrid.location.Area
import com.runemate.game.api.hybrid.location.Coordinate
import com.runemate.game.api.osrs.data.NpcID

object Houses {
	// Ardougne
	val JESS = MahoganyHome(
		owner = "Jess",
		location = Coordinate(2621, 3292, 0),
		area = Area.rectangular(Coordinate(2611, 3290, 0), Coordinate(2611 + 14, 3290 + 7, 0)),
		hint = "Upstairs of the building south of the church in East Ardougne",
		npcId = NpcID.JESS,
		isUpstairs = true,
		houseObjects = HouseObjects.JESS,
		requiredMaterials = RequiredMaterialsByTier.JESS,
		ladders = setOf(17026, 16685)
	)

	val NOELLA = MahoganyHome(
		owner = "Noella",
		location = Coordinate(2659, 3322, 0),
		area = Area.rectangular(
			 Coordinate(2652, 3317, 0),
		 Coordinate(2652 + 15, 3317 + 8, 0)
	),
		hint = "North of East Ardougne market",
		npcId = NpcID.NOELLA,
		houseObjects = HouseObjects.NOELLA,
		requiredMaterials = RequiredMaterialsByTier.NOELLA,
		ladders = setOf(17026, 16685, 15645, 15648)
	)

	val ROSS = MahoganyHome(
		owner = "Ross",
		location = Coordinate(2613, 3316, 0),
		area = Area.rectangular(
			Coordinate(2609, 3313, 0),
			Coordinate(2609 + 11, 3313 + 9, 0)
		),
		hint = "North of the church in East Ardougne",
		npcId = NpcID.ROSS,
		houseObjects = HouseObjects.ROSS,
		requiredMaterials = RequiredMaterialsByTier.ROSS,
		ladders = setOf(16683, 16679)
	)

	// Falador
	val LARRY = MahoganyHome(
		owner = "Larry",
		location = Coordinate(3038, 3364, 0),
		area = Area.rectangular(
			Coordinate(3033, 3360, 0),
			Coordinate(3033 + 10, 3360 + 9, 0)
		),
		hint = "North of the fountain in Falador",
		npcId = NpcID.LARRY_10418,
		houseObjects = HouseObjects.LARRY,
		requiredMaterials = RequiredMaterialsByTier.LARRY,
		ladders = setOf(24075, 24076)
	)

	val NORMAN = MahoganyHome(
		owner = "Norman",
		location = Coordinate(3038, 3344, 0),
		area = Area.rectangular(
			Coordinate(3034, 3341, 0),
			Coordinate(3034 + 8, 3341 + 8, 0)
		),
		hint = "South of the fountain in Falador",
		npcId = NpcID.NORMAN,
		isUpstairs = true,
		houseObjects = HouseObjects.NORMAN,
		requiredMaterials = RequiredMaterialsByTier.NORMAN,
		ladders = setOf(24082, 24085)
	)

	val TAU = MahoganyHome(
		owner = "Tau",
		location = Coordinate(3047, 3345, 0),
		area = Area.rectangular(
			Coordinate(3043, 3340, 0),
			Coordinate(3043 + 10, 3340 + 11, 0)
		),
		hint = "South east of the fountain in Falador",
		npcId = NpcID.TAU,
		requiredMaterials = RequiredMaterialsByTier.TAU,
		houseObjects = HouseObjects.TAU
	)

	// Hosidius
	val BARBARA = MahoganyHome(
		owner = "Barbara",
		location = Coordinate(1750, 3534, 0),
		area = Area.rectangular(
			Coordinate(1746, 3531, 0),
			Coordinate(1746 + 10, 3531 + 11, 0)
		),
		hint = "South of Hosidius, near the mill",
		npcId = NpcID.BARBARA,
		requiredMaterials = RequiredMaterialsByTier.BARBARA,
		houseObjects = HouseObjects.BARBARA
	)

	val LEELA = MahoganyHome(
		owner = "Leela",
		location = Coordinate(1785, 3592, 0),
		area = Area.rectangular(
			Coordinate(1781, 3589, 0),
			Coordinate(1781 + 9, 3589 + 8, 0)
		),
		hint = "East of the town market in Hosidius",
		npcId = NpcID.LEELA_10423,
		houseObjects = HouseObjects.LEELA,
		requiredMaterials = RequiredMaterialsByTier.LEELA,
		ladders = setOf(11794, 11802)
	)

	val MARIAH = MahoganyHome(
		owner = "Mariah",
		location = Coordinate(1766, 3621, 0),
		area = Area.rectangular(
			Coordinate(1762, 3618, 0),
			Coordinate(1762 + 10, 3618 + 7, 0)
		),
		hint = "West of the estate agents in Hosidius",
		npcId = NpcID.MARIAH,
		houseObjects = HouseObjects.MARIAH,
		requiredMaterials = RequiredMaterialsByTier.MARIAH,
		ladders = setOf(11794, 11802)
	)

	// Varrock
	val BOB = MahoganyHome(
		owner = "Bob",
		location = Coordinate(3238, 3486, 0),
		area = Area.rectangular(
			Coordinate(3234, 3482, 0),
			Coordinate(3234 + 10, 3482 + 10, 0)
		),
		hint = "North-east Varrock, opposite the church",
		npcId = NpcID.BOB_10414,
		houseObjects = HouseObjects.BOB,
		requiredMaterials = RequiredMaterialsByTier.BOB,
		ladders = setOf(11797, 11799)
	)

	val JEFF = MahoganyHome(
		owner = "Jeff",
		location = Coordinate(3239, 3450, 0),
		area = Area.rectangular(
			Coordinate(3235, 3445, 0),
			Coordinate(3235 + 10, 3445 + 12, 0)
		),
		hint = "Middle of Varrock, west of the museum",
		npcId = NpcID.JEFF_10415,
		houseObjects = HouseObjects.JEFF,
		requiredMaterials = RequiredMaterialsByTier.JEFF,
		ladders = setOf(11789, 11793)
	)

	val SARAH = MahoganyHome(
		owner = "Sarah",
		location = Coordinate(3235, 3384, 0),
		area = Area.rectangular(
			Coordinate(3232, 3381, 0),
			Coordinate(3232 + 8, 3381 + 7, 0)
		),
		hint = "Along the south wall of Varrock",
		npcId = NpcID.SARAH_10416,
		requiredMaterials = RequiredMaterialsByTier.SARAH,
		houseObjects = HouseObjects.SARAH
	)

	// All houses list for easy access
	val ALL_HOUSES = listOf(
		// Ardougne
		JESS,
		NOELLA,
		ROSS,
		// Falador
		LARRY,
		NORMAN,
		TAU,
		// Hosidius
		BARBARA,
		LEELA,
		MARIAH,
		// Varrock
		BOB,
		JEFF,
		SARAH
	)

	val HOSIDIOUS_HOUSES = listOf(BARBARA, LEELA, MARIAH)
	val ARDOUGNE_HOUSES = listOf(JESS, NOELLA, ROSS)
	val FALADOR_HOUSES = listOf(LARRY, NORMAN, TAU)
	val VARROCK_HOUSES = listOf(BOB, JEFF, SARAH)
}