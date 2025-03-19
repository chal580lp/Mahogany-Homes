package com.recursive.mahoganyhomes.common

import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.input.direct.DirectInput
import com.runemate.game.api.hybrid.input.direct.MenuAction
import java.util.regex.Pattern

fun hoverAndDI(gameObj: GameObject, action: String) {
    gameObj.hover()
    DirectInput.send(MenuAction.forGameObject(gameObj, action))
}

fun hoverAndDI(gameObj: GameObject, action: Pattern) {
    gameObj.hover()
    DirectInput.send(MenuAction.forGameObject(gameObj, action))
}

fun hoverAndDI(npc: Npc, action: Pattern) {
    npc.hover()
    DirectInput.send(MenuAction.forNpc(npc, action))
}

fun hoverAndDI(npc: Npc, action: String) {
    npc.hover()
    DirectInput.send(MenuAction.forNpc(npc, action))
}

