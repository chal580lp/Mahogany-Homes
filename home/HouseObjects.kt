package com.recursive.mahoganyhomes.home

import com.recursive.mahoganyhomes.game.RequiredMaterials

enum class HouseObjects(vararg val objects: FurnitureObject) {
	// Ardougne
	JESS(
		FurnitureObject(40171, FurnitureType.BUILD_2, 7),
		FurnitureObject(40172, FurnitureType.BUILD_2, 1),
		FurnitureObject(40173, FurnitureType.BUILD_2, 3),
		FurnitureObject(40174, FurnitureType.BUILD_2, 2),
		FurnitureObject(40175, FurnitureType.BUILD_3, 4),
		FurnitureObject(40176, FurnitureType.BUILD_3, 5),
		FurnitureObject(40177, FurnitureType.REPAIR_PLANK, 6),
		FurnitureObject(40299, FurnitureType.REPAIR_STEEL, 8)
	),
	NOELLA(
		FurnitureObject(40156, FurnitureType.BUILD_2, 8), // R Dresser
		FurnitureObject(40157, FurnitureType.BUILD_2, 7), // R Cupboard
		FurnitureObject(40158, FurnitureType.REPAIR_PLANK, 5), // R Hat Stand
		FurnitureObject(40159, FurnitureType.REPAIR_PLANK, 6), // R Mirror
		FurnitureObject(40160, FurnitureType.BUILD_2, 4), // L Drawer ladder down 16685
		FurnitureObject(40161, FurnitureType.BUILD_3, 2), // L Table
		FurnitureObject(40162, FurnitureType.BUILD_3, 3), // L Table
		FurnitureObject(40163, FurnitureType.REPAIR_PLANK, 1) // L Clock
	),
	ROSS(
		FurnitureObject(40164, FurnitureType.REPAIR_STEEL, 3),
		FurnitureObject(40165, FurnitureType.BUILD_2, 1),
		FurnitureObject(40166, FurnitureType.BUILD_2, 2),
		FurnitureObject(40167, FurnitureType.BUILD_3, 7),
		FurnitureObject(40168, FurnitureType.REPAIR_PLANK, 6),
		FurnitureObject(40169, FurnitureType.BUILD_2, 4),
		FurnitureObject(40170, FurnitureType.REPAIR_PLANK, 5)
	),

	// Falador
	LARRY(
		FurnitureObject(40297, FurnitureType.REPAIR_STEEL, 4),
		FurnitureObject(40095, FurnitureType.BUILD_2, 2),
		FurnitureObject(40096, FurnitureType.BUILD_2, 1),
		FurnitureObject(40097, FurnitureType.BUILD_3, 5),
		FurnitureObject(40298, FurnitureType.REPAIR_PLANK, 7),
		FurnitureObject(40098, FurnitureType.BUILD_3, 6),
		FurnitureObject(40099, FurnitureType.REPAIR_PLANK, 3)
	),
	NORMAN(
		FurnitureObject(40296, FurnitureType.REPAIR_STEEL, 1),
		FurnitureObject(40089, FurnitureType.REPAIR_PLANK, 2),
		FurnitureObject(40090, FurnitureType.BUILD_3, 3),
		FurnitureObject(40091, FurnitureType.BUILD_3, 7),
		FurnitureObject(40092, FurnitureType.BUILD_2, 4),
		FurnitureObject(40093, FurnitureType.BUILD_2, 5),
		FurnitureObject(40094, FurnitureType.BUILD_2, 6)
	),
	TAU(
		FurnitureObject(40083, FurnitureType.REPAIR_STEEL, 2),
		FurnitureObject(40084, FurnitureType.BUILD_3, 3),
		FurnitureObject(40085, FurnitureType.BUILD_3, 1),
		FurnitureObject(40086, FurnitureType.BUILD_2, 4),
		FurnitureObject(40087, FurnitureType.BUILD_2, 6),
		FurnitureObject(40088, FurnitureType.BUILD_2, 7),
		FurnitureObject(40295, FurnitureType.REPAIR_PLANK, 5)
	),

	// Hosidius
	BARBARA(
		FurnitureObject(40011, FurnitureType.REPAIR_PLANK, 2),
		FurnitureObject(40293, FurnitureType.REPAIR_STEEL, 4),
		FurnitureObject(40012, FurnitureType.BUILD_3, 3),
		FurnitureObject(40294, FurnitureType.BUILD_2, 7),
		FurnitureObject(40013, FurnitureType.BUILD_2, 1),
		FurnitureObject(40014, FurnitureType.BUILD_1, 6),
		FurnitureObject(40015, FurnitureType.BUILD_1, 5)
	),
	LEELA(
		FurnitureObject(40007, FurnitureType.BUILD_2, 2),
		FurnitureObject(40008, FurnitureType.BUILD_2, 3),
		FurnitureObject(40290, FurnitureType.REPAIR_STEEL, 1),
		FurnitureObject(40291, FurnitureType.BUILD_3, 4),
		FurnitureObject(40009, FurnitureType.BUILD_3, 7),
		FurnitureObject(40010, FurnitureType.REPAIR_PLANK, 6),
		FurnitureObject(40292, FurnitureType.BUILD_2, 5)
	),
	MARIAH(
		FurnitureObject(40002, FurnitureType.BUILD_3, 1),
		FurnitureObject(40287, FurnitureType.REPAIR_STEEL, 3),
		FurnitureObject(40003, FurnitureType.BUILD_2, 5),
		FurnitureObject(40288, FurnitureType.BUILD_2, 4),
		FurnitureObject(40004, FurnitureType.BUILD_2, 7),
		FurnitureObject(40005, FurnitureType.BUILD_2, 6),
		FurnitureObject(40006, FurnitureType.BUILD_2, 8),
		FurnitureObject(40289, FurnitureType.REPAIR_PLANK, 2)
	),

	// Varrock
	BOB(
		FurnitureObject(39981, FurnitureType.BUILD_4, 1),
		FurnitureObject(39982, FurnitureType.REPAIR_PLANK, 2),
		FurnitureObject(39983, FurnitureType.BUILD_2, 4),
		FurnitureObject(39984, FurnitureType.BUILD_2, 5),
		FurnitureObject(39985, FurnitureType.BUILD_2, 3),
		FurnitureObject(39986, FurnitureType.BUILD_2, 6),
		FurnitureObject(39987, FurnitureType.BUILD_2, 7),
		FurnitureObject(39988, FurnitureType.BUILD_2, 8)
	),
	JEFF(
		FurnitureObject(39989, FurnitureType.BUILD_3, 2),
		FurnitureObject(39990, FurnitureType.BUILD_2, 1),
		FurnitureObject(39991, FurnitureType.BUILD_2, 3),
		FurnitureObject(39992, FurnitureType.BUILD_3, 7),
		FurnitureObject(39993, FurnitureType.BUILD_2, 6),
		FurnitureObject(39994, FurnitureType.BUILD_2, 4),
		FurnitureObject(39995, FurnitureType.REPAIR_PLANK, 8),
		FurnitureObject(39996, FurnitureType.BUILD_1, 5)
	),
	SARAH(
		FurnitureObject(39997, FurnitureType.BUILD_3, 1),
		FurnitureObject(39998, FurnitureType.BUILD_2, 2),
		FurnitureObject(39999, FurnitureType.BUILD_2, 3),
		FurnitureObject(40000, FurnitureType.BUILD_2, 4),
		FurnitureObject(40286, FurnitureType.REPAIR_STEEL, 5),
		FurnitureObject(40001, FurnitureType.BUILD_2, 6)
	);

	fun getRequiredMaterialsForVarbs(repairableVarbs: Set<Int>): RequiredMaterials {
		val required = RequiredMaterials(0, 0, 0, 0)
		val startingVarb = FurnitureSlot.SLOT_1.varb

		objects.forEachIndexed { index, obj ->
			if (startingVarb + index in repairableVarbs) {
				when (obj.type.material) {
					MaterialType.PLANK -> {
						required.minPlanks += obj.type.materialAmount
						required.maxPlanks += obj.type.materialAmount
					}

					MaterialType.STEEL_BAR -> {
						required.minSteelBars += obj.type.materialAmount
						required.maxSteelBars += obj.type.materialAmount
					}
				}
			}
		}

		return required
	}

	fun getOrderOfObject(objectId: Int): Int {
		return objects.find { it.objectId == objectId }?.order ?: Int.MAX_VALUE
	}

}