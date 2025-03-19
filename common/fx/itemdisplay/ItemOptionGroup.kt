package com.recursive.mahoganyhomes.common.fx.itemdisplay

data class ItemOptionGroup(
	val description: String? = null,
	val options: List<ItemData>,
	val usedFor: Set<UsedFor> = emptySet()
) {
	companion object {
		fun single(
			item: ItemData,
			vararg usedFor: UsedFor
		) = ItemOptionGroup(
			options = listOf(item),
			usedFor = usedFor.toSet()
		)

		fun fromList(
			description: String,
			items: List<ItemData>,
			vararg usedFor: UsedFor
		) = ItemOptionGroup(
			description = description,
			options = items,
			usedFor = usedFor.toSet()
		)
	}
}

data class ItemRequirement(
	val groups: List<ItemOptionGroup>
) {
	companion object {
		fun single(
			description: String? = null,
			item: ItemData
		) = ItemRequirement(listOf(ItemOptionGroup.single(item = item)))

		fun multiple(vararg groups: ItemOptionGroup) = ItemRequirement(groups.toList())
	}
}