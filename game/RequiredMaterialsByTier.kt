package com.recursive.mahoganyhomes.game

enum class RequiredMaterialsByTier(
	private val beginner: RequiredMaterials,
	private val novice: RequiredMaterials,
	private val adept: RequiredMaterials,
	private val expert: RequiredMaterials
) {
	// Ardougne
	JESS(
		RequiredMaterials(9, 11, 0, 1),
		RequiredMaterials(9, 11, 0, 1),
		RequiredMaterials(12, 15, 0, 1),
		RequiredMaterials(14, 15, 0, 1)
	),
	NOELLA(
		RequiredMaterials(11, 12, 0, 0),
		RequiredMaterials(11, 12, 0, 0),
		RequiredMaterials(12, 15, 0, 0),
		RequiredMaterials(13, 15, 0, 0)
	),
	ROSS(
		RequiredMaterials(8, 11, 0, 1),
		RequiredMaterials(8, 11, 0, 1),
		RequiredMaterials(8, 11, 1, 1),
		RequiredMaterials(10, 11, 0, 1)
	),
	// Falador
	LARRY(
		RequiredMaterials(8, 12, 0, 1),
		RequiredMaterials(8, 12, 0, 1),
		RequiredMaterials(9, 12, 0, 1),
		RequiredMaterials(12, 12, 0, 1)
	),
	NORMAN(
		RequiredMaterials(11, 11, 0, 1),
		RequiredMaterials(10, 11, 0, 1),
		RequiredMaterials(10, 13, 0, 1),
		RequiredMaterials(12, 13, 0, 1)
	),
	TAU(
		RequiredMaterials(8, 12, 0, 1),
		RequiredMaterials(8, 12, 0, 1),
		RequiredMaterials(9, 13, 0, 1),
		RequiredMaterials(12, 13, 0, 1)
	),
	// Hosidius
	BARBARA(
		RequiredMaterials(3, 8, 0, 1),
		RequiredMaterials(3, 8, 0, 1),
		RequiredMaterials(9, 10, 0, 1),
		RequiredMaterials(9, 10, 0, 1)
	),
	LEELA(
		RequiredMaterials(8, 9, 0, 1),
		RequiredMaterials(8, 9, 0, 1),
		RequiredMaterials(9, 10, 0, 1),
		RequiredMaterials(12, 13, 0, 1)
	),
	MARIAH(
		RequiredMaterials(7, 11, 0, 1),
		RequiredMaterials(7, 11, 0, 1),
		RequiredMaterials(11, 14, 0, 1),
		RequiredMaterials(13, 14, 0, 1)
	),
	// Varrock
	BOB(
		RequiredMaterials(13, 14, 0, 0),
		RequiredMaterials(13, 14, 0, 0),
		RequiredMaterials(13, 17, 0, 0),
		RequiredMaterials(16, 17, 0, 0)
	),
	JEFF(
		RequiredMaterials(11, 13, 0, 0),
		RequiredMaterials(11, 13, 0, 0),
		RequiredMaterials(11, 16, 0, 0),
		RequiredMaterials(15, 16, 0, 0)
	),
	SARAH(
		RequiredMaterials(11, 11, 0, 1),
		RequiredMaterials(11, 11, 0, 1),
		RequiredMaterials(11, 11, 0, 1),
		RequiredMaterials(11, 11, 0, 1)
	);

	fun getByTier(tier: Int): RequiredMaterials? = when(tier) {
		1 -> beginner
		2 -> novice
		3 -> adept
		4 -> expert
		else -> null
	}
}