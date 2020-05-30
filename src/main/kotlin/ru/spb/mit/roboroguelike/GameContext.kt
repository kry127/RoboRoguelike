package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent
import ru.spb.mit.roboroguelike.objects.Player


data class GameContext(val world: World, // 1
                       val screen: Screen,  // 2
                       val uiEvent: UIEvent, // 3
                       val player: GameEntity<Player>) : Context //