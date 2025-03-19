package com.recursive.mahoganyhomes.common.fx.itemdisplay

import com.runemate.game.api.hybrid.local.Quest
import com.runemate.game.api.hybrid.local.Skill
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory

sealed interface RequirementData {
	fun isMet(): Boolean

	data class InventoryRequirement(val itemName: String, val quantity: Int = 1) : RequirementData {
		override fun isMet(): Boolean = Inventory.getQuantity(itemName) >= quantity
		override fun toString(): String = "$itemName x$quantity"
	}

	data class QuestRequirement(val quest: Quest) : RequirementData {
		override fun isMet(): Boolean = quest.status == Quest.Status.COMPLETE
		override fun toString(): String = quest.name
	}

	data class SkillRequirement(val skill: Skill, val level: Int) : RequirementData {
		override fun isMet(): Boolean = skill.baseLevel >= level
		override fun toString(): String = "$level $skill"
	}

	data class MultiRequirement(val requirements: List<RequirementData>) : RequirementData {
		override fun isMet(): Boolean = requirements.all { it.isMet() }
	}

	data class TextRequirement(val text: String) : RequirementData {
		override fun isMet(): Boolean = true
		override fun toString(): String = text
	}
}
