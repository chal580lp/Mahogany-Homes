package com.recursive.mahoganyhomes.contract

data class ContractStats(
	var sessionContracts: Int = 0,
	var sessionPoints: Int = 0
) {
	fun getPointsForCompletingTask(tier: Int): Int {
		// Contracts reward 2-5 points depending on tier
		return tier + 1
	}
}