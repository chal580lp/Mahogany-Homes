package com.recursive.mahoganyhomes.home

enum class FurnitureType(
	val material: MaterialType,
	val materialAmount: Int
) {
	// Remove & Build Furniture (1-4 planks)
	BUILD_1(MaterialType.PLANK, 1),
	BUILD_2(MaterialType.PLANK, 2),
	BUILD_3(MaterialType.PLANK, 3),
	BUILD_4(MaterialType.PLANK, 4),
	// Repair Furniture
	REPAIR_PLANK(MaterialType.PLANK, 1),
	REPAIR_STEEL(MaterialType.STEEL_BAR, 1)
}

