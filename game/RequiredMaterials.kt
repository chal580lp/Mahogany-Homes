package com.recursive.mahoganyhomes.game

data class RequiredMaterials(
	var minPlanks: Int,
	var maxPlanks: Int,
	var minSteelBars: Int = 0,
	var maxSteelBars: Int = 0
)