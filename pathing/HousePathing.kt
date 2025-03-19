package com.recursive.mahoganyhomes.pathing

import com.recursive.mahoganyhomes.contract.Contractor
import com.recursive.mahoganyhomes.home.Houses
import com.recursive.mahoganyhomes.home.MahoganyHome

data class HousePathing(
	val mahoganyHome: MahoganyHome,
	val contractorToBankToTPToHouse: Double,
	val teleportToBankToHouse: Double
) {
	fun fasterOption(): Option {
		return if (contractorToBankToTPToHouse < teleportToBankToHouse) Option.WALK_TP_HOUSE else Option.TELE_BANK_HOUSE
	}
	enum class Option {
		WALK_TP_HOUSE,
		TELE_BANK_HOUSE
	}

	fun isSameCityPath(contractor: Contractor): Boolean {
		return when (contractor) {
			Contractor.ELLIE -> mahoganyHome in Houses.ARDOUGNE_HOUSES
			Contractor.AMY -> mahoganyHome in Houses.FALADOR_HOUSES
			Contractor.ANGELO -> mahoganyHome in Houses.HOSIDIOUS_HOUSES
			Contractor.MARLO -> mahoganyHome in Houses.VARROCK_HOUSES
		}
	}
}