package com.recursive.mahoganyhomes.pathing

import com.recursive.mahoganyhomes.contract.Contractor
import com.recursive.mahoganyhomes.home.Houses
import com.recursive.mahoganyhomes.home.MahoganyHome

object HousePaths {
	val CONTRACTOR_PATHS: List<ContractorPaths> = listOf(
		ContractorPaths(
			contractor = Contractor.ELLIE,
			contractorToBank = 25.0, // Ellie to Ardougne Bank
			housePathing = listOf(
				HousePathing(Houses.JESS,
					contractorToBankToTPToHouse = 73.0, // Ard Bank (25) -> Ard Tele -> Jess (48)
					teleportToBankToHouse = 85.0 // Ard Tele -> Bank (37) -> Jess (48)
				),
				HousePathing(Houses.NOELLA,
					contractorToBankToTPToHouse = 47.0, // Ard Bank (25) -> Ard Tele -> Noella (22)
					teleportToBankToHouse = 59.0 // Ard Tele -> Bank (37) -> Noella (22)
				),
				HousePathing(Houses.ROSS,
					contractorToBankToTPToHouse = 87.0, // Ard Bank (25) -> Ard Tele -> Ross (62)
					teleportToBankToHouse = 99.0 // Ard Tele -> Bank (37) -> Ross (62)
				),
				HousePathing(Houses.LARRY,
					contractorToBankToTPToHouse = 112.0, // Ard Bank (25) -> Fal Tele -> Larry (87)
					teleportToBankToHouse = 134.0 // Fal Tele -> Bank (47) -> Larry (87)
				),
				HousePathing(Houses.NORMAN,
					contractorToBankToTPToHouse = 132.0, // Ard Bank (25) -> Fal Tele -> Norman (107)
					teleportToBankToHouse = 154.0 // Fal Tele -> Bank (47) -> Norman (107)
				),
				HousePathing(Houses.TAU,
					contractorToBankToTPToHouse = 140.0, // Ard Bank (25) -> Fal Tele -> Tau (115)
					teleportToBankToHouse = 162.0 // Fal Tele -> Bank (47) -> Tau (115)
				),
				HousePathing(Houses.BARBARA,
					contractorToBankToTPToHouse = 47.0, // Ard Bank (25) -> Hos Tele -> Barbara (22)
					teleportToBankToHouse = 150.0 // Hos Tele -> Bank (86) -> Barbara (64)
				),
				HousePathing(Houses.LEELA,
					contractorToBankToTPToHouse = 140.0, // Ard Bank (25) -> Hos Tele -> Leela (115)
					teleportToBankToHouse = 201.0 // Hos Tele -> Bank (86) -> Leela (115)
				),
				HousePathing(Houses.MARIAH,
					contractorToBankToTPToHouse = 150.0, // Ard Bank (25) -> Hos Tele -> Mariah (125)
					teleportToBankToHouse = 211.0 // Hos Tele -> Bank (86) -> Mariah (125)
				),
				HousePathing(Houses.BOB,
					contractorToBankToTPToHouse = 115.0, // Ard Bank (25) -> Var Tele -> Bob (90)
					teleportToBankToHouse = 132.0 // Var Tele -> Bank (42) -> Bob (90)
				),
				HousePathing(Houses.JEFF,
					contractorToBankToTPToHouse = 80.0, // Ard Bank (25) -> Var Tele -> Jeff (55)
					teleportToBankToHouse = 97.0 // Var Tele -> Bank (42) -> Jeff (55)
				),
				HousePathing(Houses.SARAH,
					contractorToBankToTPToHouse = 88.0, // Ard Bank (25) -> Var Tele -> Sarah (63)
					teleportToBankToHouse = 105.0 // Var Tele -> Bank (42) -> Sarah (63)
				)
			)
		),
		ContractorPaths(
			contractor = Contractor.AMY,
			contractorToBank = 32.0, // Amy to Falador Bank
			housePathing = listOf(
				HousePathing(Houses.JESS,
					contractorToBankToTPToHouse = 80.0, // Fal Bank (32) -> Ard Tele -> Jess (48)
					teleportToBankToHouse = 85.0 // Ard Tele -> Bank (37) -> Jess (48)
				),
				HousePathing(Houses.NOELLA,
					contractorToBankToTPToHouse = 54.0, // Fal Bank (32) -> Ard Tele -> Noella (22)
					teleportToBankToHouse = 59.0 // Ard Tele -> Bank (37) -> Noella (22)
				),
				HousePathing(Houses.ROSS,
					contractorToBankToTPToHouse = 94.0, // Fal Bank (32) -> Ard Tele -> Ross (62)
					teleportToBankToHouse = 99.0 // Ard Tele -> Bank (37) -> Ross (62)
				),
				HousePathing(Houses.LARRY,
					contractorToBankToTPToHouse = 119.0, // Fal Bank (32) -> Fal Tele -> Larry (87)
					teleportToBankToHouse = 134.0 // Fal Tele -> Bank (47) -> Larry (87)
				),
				HousePathing(Houses.NORMAN,
					contractorToBankToTPToHouse = 139.0, // Fal Bank (32) -> Fal Tele -> Norman (107)
					teleportToBankToHouse = 154.0 // Fal Tele -> Bank (47) -> Norman (107)
				),
				HousePathing(Houses.TAU,
					contractorToBankToTPToHouse = 147.0, // Fal Bank (32) -> Fal Tele -> Tau (115)
					teleportToBankToHouse = 162.0 // Fal Tele -> Bank (47) -> Tau (115)
				),
				HousePathing(Houses.BARBARA,
					contractorToBankToTPToHouse = 54.0, // Fal Bank (32) -> Hos Tele -> Barbara (22)
					teleportToBankToHouse = 150.0 // Hos Tele -> Bank (86) -> Barbara (64)
				),
				HousePathing(Houses.LEELA,
					contractorToBankToTPToHouse = 147.0, // Fal Bank (32) -> Hos Tele -> Leela (115)
					teleportToBankToHouse = 201.0 // Hos Tele -> Bank (86) -> Leela (115)
				),
				HousePathing(Houses.MARIAH,
					contractorToBankToTPToHouse = 157.0, // Fal Bank (32) -> Hos Tele -> Mariah (125)
					teleportToBankToHouse = 211.0 // Hos Tele -> Bank (86) -> Mariah (125)
				),
				HousePathing(Houses.BOB,
					contractorToBankToTPToHouse = 122.0, // Fal Bank (32) -> Var Tele -> Bob (90)
					teleportToBankToHouse = 132.0 // Var Tele -> Bank (42) -> Bob (90)
				),
				HousePathing(Houses.JEFF,
					contractorToBankToTPToHouse = 87.0, // Fal Bank (32) -> Var Tele -> Jeff (55)
					teleportToBankToHouse = 97.0 // Var Tele -> Bank (42) -> Jeff (55)
				),
				HousePathing(Houses.SARAH,
					contractorToBankToTPToHouse = 95.0, // Fal Bank (32) -> Var Tele -> Sarah (63)
					teleportToBankToHouse = 105.0 // Var Tele -> Bank (42) -> Sarah (63)
				)
			)
		),
		ContractorPaths(
			contractor = Contractor.ANGELO,
			contractorToBank = 60.0, // Angelo to Hosidius Bank
			housePathing = listOf(
				HousePathing(Houses.JESS,
					contractorToBankToTPToHouse = 108.0, // Hos Bank (60) -> Ard Tele -> Jess (48)
					teleportToBankToHouse = 85.0 // Ard Tele -> Bank (37) -> Jess (48)
				),
				HousePathing(Houses.NOELLA,
					contractorToBankToTPToHouse = 82.0, // Hos Bank (60) -> Ard Tele -> Noella (22)
					teleportToBankToHouse = 59.0 // Ard Tele -> Bank (37) -> Noella (22)
				),
				HousePathing(Houses.ROSS,
					contractorToBankToTPToHouse = 122.0, // Hos Bank (60) -> Ard Tele -> Ross (62)
					teleportToBankToHouse = 99.0 // Ard Tele -> Bank (37) -> Ross (62)
				),
				HousePathing(Houses.LARRY,
					contractorToBankToTPToHouse = 147.0, // Hos Bank (60) -> Fal Tele -> Larry (87)
					teleportToBankToHouse = 134.0 // Fal Tele -> Bank (47) -> Larry (87)
				),
				HousePathing(Houses.NORMAN,
					contractorToBankToTPToHouse = 167.0, // Hos Bank (60) -> Fal Tele -> Norman (107)
					teleportToBankToHouse = 154.0 // Fal Tele -> Bank (47) -> Norman (107)
				),
				HousePathing(Houses.TAU,
					contractorToBankToTPToHouse = 175.0, // Hos Bank (60) -> Fal Tele -> Tau (115)
					teleportToBankToHouse = 162.0 // Fal Tele -> Bank (47) -> Tau (115)
				),
				HousePathing(Houses.BARBARA,
					contractorToBankToTPToHouse = 82.0, // Hos Bank (60) -> Hos Tele -> Barbara (22)
					teleportToBankToHouse = 150.0 // Hos Tele -> Bank (86) -> Barbara (64)
				),
				HousePathing(Houses.LEELA,
					contractorToBankToTPToHouse = 175.0, // Hos Bank (60) -> Hos Tele -> Leela (115)
					teleportToBankToHouse = 201.0 // Hos Tele -> Bank (86) -> Leela (115)
				),
				HousePathing(Houses.MARIAH,
					contractorToBankToTPToHouse = 185.0, // Hos Bank (60) -> Hos Tele -> Mariah (125)
					teleportToBankToHouse = 211.0 // Hos Tele -> Bank (86) -> Mariah (125)
				),
				HousePathing(Houses.BOB,
					contractorToBankToTPToHouse = 150.0, // Hos Bank (60) -> Var Tele -> Bob (90)
					teleportToBankToHouse = 132.0 // Var Tele -> Bank (42) -> Bob (90)
				),
				HousePathing(Houses.JEFF,
					contractorToBankToTPToHouse = 115.0, // Hos Bank (60) -> Var Tele -> Jeff (55)
					teleportToBankToHouse = 97.0 // Var Tele -> Bank (42) -> Jeff (55)
				),
				HousePathing(Houses.SARAH,
					contractorToBankToTPToHouse = 123.0, // Hos Bank (60) -> Var Tele -> Sarah (63)
					teleportToBankToHouse = 105.0 // Var Tele -> Bank (42) -> Sarah (63)
				)
			)
		),
		ContractorPaths(
			contractor = Contractor.MARLO,
			contractorToBank = 65.0, // Marlo to Varrock Bank
			housePathing = listOf(
				HousePathing(Houses.JESS,
					contractorToBankToTPToHouse = 113.0, // Var Bank (65) -> Ard Tele -> Jess (48)
					teleportToBankToHouse = 85.0 // Ard Tele -> Bank (37) -> Jess (48)
				),
				HousePathing(Houses.NOELLA,
					contractorToBankToTPToHouse = 87.0, // Var Bank (65) -> Ard Tele -> Noella (22)
					teleportToBankToHouse = 59.0 // Ard Tele -> Bank (37) -> Noella (22)
				),
				HousePathing(Houses.ROSS,
					contractorToBankToTPToHouse = 127.0, // Var Bank (65) -> Ard Tele -> Ross (62)
					teleportToBankToHouse = 99.0 // Ard Tele -> Bank (37) -> Ross (62)
				),
				HousePathing(Houses.LARRY,
					contractorToBankToTPToHouse = 152.0, // Var Bank (65) -> Fal Tele -> Larry (87)
					teleportToBankToHouse = 134.0 // Fal Tele -> Bank (47) -> Larry (87)
				),
				HousePathing(Houses.NORMAN,
					contractorToBankToTPToHouse = 172.0, // Var Bank (65) -> Fal Tele -> Norman (107)
					teleportToBankToHouse = 154.0 // Fal Tele -> Bank (47) -> Norman (107)
				),
				HousePathing(Houses.TAU,
					contractorToBankToTPToHouse = 180.0, // Var Bank (65) -> Fal Tele -> Tau (115)
					teleportToBankToHouse = 162.0 // Fal Tele -> Bank (47) -> Tau (115)
				),
				HousePathing(Houses.BARBARA,
					contractorToBankToTPToHouse = 87.0, // Var Bank (65) -> Hos Tele -> Barbara (22)
					teleportToBankToHouse = 150.0 // Hos Tele -> Bank (86) -> Barbara (64)
				),
				HousePathing(Houses.LEELA,
					contractorToBankToTPToHouse = 180.0, // Var Bank (65) -> Hos Tele -> Leela (115)
					teleportToBankToHouse = 201.0 // Hos Tele -> Bank (86) -> Leela (115)
				),
				HousePathing(Houses.MARIAH,
					contractorToBankToTPToHouse = 190.0, // Var Bank (65) -> Hos Tele -> Mariah (125)
					teleportToBankToHouse = 211.0 // Hos Tele -> Bank (86) -> Mariah (125)
				),
				HousePathing(Houses.BOB,
					contractorToBankToTPToHouse = 155.0, // Var Bank (65) -> Var Tele -> Bob (90)
					teleportToBankToHouse = 132.0 // Var Tele -> Bank (42) -> Bob (90)
				),
				HousePathing(Houses.JEFF,
					contractorToBankToTPToHouse = 120.0, // Var Bank (65) -> Var Tele -> Jeff (55)
					teleportToBankToHouse = 97.0 // Var Tele -> Bank (42) -> Jeff (55)
				),
				HousePathing(Houses.SARAH,
					contractorToBankToTPToHouse = 128.0, // Var Bank (65) -> Var Tele -> Sarah (63)
					teleportToBankToHouse = 105.0 // Var Tele -> Bank (42) -> Sarah (63)
				)
			)
		)
	)

	fun getHousePath(contractor: Contractor, mahoganyHome: MahoganyHome): HousePathing? {
		return CONTRACTOR_PATHS
			.find { it.contractor == contractor }
			?.housePathing
			?.find { it.mahoganyHome == mahoganyHome }
	}
}