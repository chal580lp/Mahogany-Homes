package com.recursive.mahoganyhomes.common.fx.itemdisplay

import com.runemate.game.api.hybrid.local.Quest
import com.runemate.game.api.hybrid.local.Skill

object ItemDataImpl {


	val HAMMER = ItemData("Hammer", id = 2347)
	val IMCANDO_HAMMER = ItemData("Imcando hammer", id = 25644)
	val IMCANDO_HAMMER_OFFHAND = ItemData("Imcando hammer (off-hand)", id = 29775)
	val ALL_HAMMERS = listOf(HAMMER, IMCANDO_HAMMER, IMCANDO_HAMMER_OFFHAND)

	val SAW = ItemData("Saw", id = 8794)
	val CRYSTAL_SAW = ItemData("Crystal saw", id = 9625)
	val AMY_SAW = ItemData("Amy's saw", id = 24880)
	val AMY_SAW_OFFHAND = ItemData("Amy's saw (off-hand)", id = 29774)
	val ALL_SAWS = listOf(SAW, CRYSTAL_SAW, AMY_SAW, AMY_SAW_OFFHAND)

	val PLANK = ItemData("Plank", id = 960)
	val OAK_PLANK = ItemData("Oak plank", id = 8778)
	val TEAK_PLANK = ItemData("Teak plank", id = 8780)
	val MAHOGANY_PLANK = ItemData("Mahogany plank", id = 8782)
	val ALL_PLANKS = listOf(PLANK, OAK_PLANK, TEAK_PLANK, MAHOGANY_PLANK)

	val STEEL_BAR = ItemData("Steel bar", id = 2353)

	val AIR_RUNE = ItemData("Air rune", id = 556)
	val EARTH_RUNE = ItemData("Earth rune", id = 557)
	val WATER_RUNE = ItemData("Water rune", id = 555)
	val FIRE_RUNE = ItemData("Fire rune", id = 554)
	val LAW_RUNE = ItemData("Law rune", id = 563)

	// Base hunting equipment
	val BIRD_SNARE = ItemData("Bird snare", id = 10006, quantity = 5)
	val BOX_TRAP = ItemData("Box trap", id = 10008, quantity = 5)
	val ROPE = ItemData("Rope", id = 954, quantity = 5)
	val SMALL_FISHING_NET = ItemData("Small fishing net", id = 303, quantity = 5)
	val BUTTERFLY_NET = ItemData("Butterfly net", id = 10010)
	val TEASING_STICK = ItemData("Teasing stick", id = 10029)
	val FALCONRY_COINS = ItemData("Coins", id = 995, quantity = 500)
	val KNIFE = ItemData("Knife", id = 946)
	val NOOSE_WAND = ItemData("Noose wand", id = 10150)

	// Desert equipment
	val WATERSKIN_4 = ItemData("Waterskin(4)", id = 1823, quantity = 5)

	// Fairy ring access items
	val DRAMEN_STAFF = ItemData(
		name = "Dramen staff",
		id = 772,
		requirements = listOf(RequirementData.QuestRequirement(Quest.OSRS.LOST_CITY))
	)

	val LUNAR_STAFF = ItemData(
		name = "Lunar staff",
		id = 9084,
		requirements = listOf(
			RequirementData.QuestRequirement(Quest.OSRS.LUNAR_DIPLOMACY)
		)
	)

	val ARDOUGNE_CLOAK_1 = ItemData(
		name = "Ardougne cloak",
		id = 13121,
	)
	val ARDOUGNE_CLOAK_2 = ItemData(
		name = "Ardougne cloak",
		id = 13122,
	)
	val ARDOUGNE_CLOAK_3 = ItemData(
		name = "Ardougne cloak",
		id = 13123,
	)
	val ARDOUGNE_CLOAK_4 = ItemData(
		name = "Ardougne cloak",
		id = 13124,
	)

	val DIGSITE_PENDANT = ItemData(
		name = "Digsite pendant",
		id = 11194,
		requirements = listOf(RequirementData.QuestRequirement(Quest.OSRS.BONE_VOYAGE))
	)
	val MOUNTED_DIGSITE_PENDANT = ItemData(
		name = "Mounted Digsite pendant",
		id = 22709,
		requirements = listOf(
			RequirementData.QuestRequirement(Quest.OSRS.BONE_VOYAGE), RequirementData.SkillRequirement(
				Skill.CONSTRUCTION, 82
			)
		)
	)

	// Axes
	val BRONZE_AXE = ItemData("Bronze axe", id = 1351)
	val IRON_AXE = ItemData("Iron axe", id = 1349)
	val STEEL_AXE = ItemData(
		name = "Steel axe",
		id = 1353,
		requirements = listOf(RequirementData.SkillRequirement(Skill.WOODCUTTING, 6))
	)
	val MITHRIL_AXE = ItemData(
		name = "Mithril axe",
		id = 1355,
		requirements = listOf(RequirementData.SkillRequirement(Skill.WOODCUTTING, 21))
	)
	val ADAMANT_AXE = ItemData(
		name = "Adamant axe",
		id = 1357,
		requirements = listOf(RequirementData.SkillRequirement(Skill.WOODCUTTING, 31))
	)
	val RUNE_AXE = ItemData(
		name = "Rune axe",
		id = 1359,
		requirements = listOf(RequirementData.SkillRequirement(Skill.WOODCUTTING, 41))
	)
	val DRAGON_AXE = ItemData(
		name = "Dragon axe",
		id = 6739,
		requirements = listOf(RequirementData.SkillRequirement(Skill.WOODCUTTING, 61))
	)
	val CRYSTAL_AXE = ItemData(
		name = "Crystal axe",
		id = 23673,
		requirements = listOf(RequirementData.SkillRequirement(Skill.WOODCUTTING, 71))
	)

	val ALL_AXES = listOf(
		BRONZE_AXE, IRON_AXE, STEEL_AXE, MITHRIL_AXE, ADAMANT_AXE, RUNE_AXE, DRAGON_AXE, CRYSTAL_AXE
	)

	val BASIC_QUETZAL_WHISTLE = ItemData(
		name = "Basic quetzal whistle",
		id = 29271,
		requirements = listOf(RequirementData.TextRequirement("10 Rumours Completed"))
	)
	val ENHANCED_QUETZAL_WHISTLE = ItemData(
		name = "Enhanced quetzal whistle",
		id = 29273,
		requirements = listOf(RequirementData.TextRequirement("10 Rumours Completed"))
	)
	val PERFECTED_QUETZAL_WHISTLE = ItemData(
		name = "Perfected quetzal whistle",
		id = 29275,
		requirements = listOf(RequirementData.TextRequirement("10 Rumours Completed"))
	)

	val ALL_QUETZAL_WHISTLES = listOf(
		BASIC_QUETZAL_WHISTLE, ENHANCED_QUETZAL_WHISTLE, PERFECTED_QUETZAL_WHISTLE
	)

	val SMALL_MEAT_POUCH = ItemData(
		name = "Small meat pouch",
		id = 29295,
		requirements = listOf(
			RequirementData.SkillRequirement(Skill.CRAFTING, 35),
		)
	)

	val LARGE_MEAT_POUCH = ItemData(
		name = "Large meat pouch",
		id = 29297,
		requirements = listOf(RequirementData.SkillRequirement(Skill.CRAFTING, 65))
	)

	val ALL_MEAT_POUCHES = listOf(
		SMALL_MEAT_POUCH, LARGE_MEAT_POUCH
	)

	val SMALL_FUR_POUCH = ItemData(
		name = "Small fur pouch",
		id = 29299,
		requirements = listOf(RequirementData.SkillRequirement(Skill.CRAFTING, 35))
	)

	val MEDIUM_FUR_POUCH = ItemData(
		name = "Medium fur pouch",
		id = 29301,
		requirements = listOf(RequirementData.SkillRequirement(Skill.CRAFTING, 50))
	)

	val LARGE_FUR_POUCH = ItemData(
		name = "Large fur pouch",
		id = 29303,
		requirements = listOf(RequirementData.SkillRequirement(Skill.CRAFTING, 65))
	)

	val ALL_FUR_POUCHES = listOf(
		SMALL_FUR_POUCH, MEDIUM_FUR_POUCH, LARGE_FUR_POUCH
	)

	val HUNTSMAN_KIT = ItemData(
		name = "Huntsman kit",
		id = 29309
	)


}