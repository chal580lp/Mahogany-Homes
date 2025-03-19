package com.recursive.mahoganyhomes.home

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet

enum class FurnitureSlot(
	val varb: Int,
	val objectIds: ImmutableSet<Int>
) {
	SLOT_1(10554, ImmutableSet.of(39981, 39989, 39997, 40002, 40007, 40011, 40083, 40156, 40164, 40171, 40296, 40297)),
	SLOT_2(10555, ImmutableSet.of(39982, 39990, 39998, 40008, 40084, 40089, 40095, 40157, 40165, 40172, 40287, 40293)),
	SLOT_3(10556, ImmutableSet.of(39983, 39991, 39999, 40003, 40012, 40085, 40090, 40096, 40158, 40166, 40173, 40290)),
	SLOT_4(10557, ImmutableSet.of(39984, 39992, 40000, 40086, 40091, 40097, 40159, 40167, 40174, 40288, 40291, 40294)),
	SLOT_5(10558, ImmutableSet.of(39985, 39993, 40004, 40009, 40013, 40087, 40092, 40160, 40168, 40175, 40286, 40298)),
	SLOT_6(10559, ImmutableSet.of(39986, 39994, 40001, 40005, 40010, 40014, 40088, 40093, 40098, 40161, 40169, 40176)),
	SLOT_7(10560, ImmutableSet.of(39987, 39995, 40006, 40015, 40094, 40099, 40162, 40170, 40177, 40292, 40295)),
	SLOT_8(10561, ImmutableSet.of(39988, 39996, 40163, 40289, 40299));

	companion object {
		private val SLOT_BY_OBJECT_ID: ImmutableMap<Int, FurnitureSlot> = values().flatMap { slot ->
			slot.objectIds.map { id -> id to slot }
		}.toMap().let { ImmutableMap.copyOf(it) }

		fun getByObjectId(objectId: Int): FurnitureSlot? = SLOT_BY_OBJECT_ID[objectId]

		fun isValidObjectId(id: Int): Boolean = SLOT_BY_OBJECT_ID.containsKey(id)
	}
}