package ru.spb.mit.roboroguelike

import World
import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent
import ru.spb.mit.roboroguelike.objects.Player


data class GameContext(val world: World,
                       val screen: Screen,
                       val uiEvent: UIEvent,
                       val player: GameEntity<Player>) : Context