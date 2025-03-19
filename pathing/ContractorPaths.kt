package com.recursive.mahoganyhomes.pathing

import com.recursive.mahoganyhomes.contract.Contractor

data class ContractorPaths(
	val contractor: Contractor,
	val contractorToBank: Double,
	val housePathing: List<HousePathing>
)