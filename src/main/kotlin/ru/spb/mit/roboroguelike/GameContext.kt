package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.Context
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.Player

/**
 * This is a class that describes global engine context. This context persists
 * and accessible during any command and event processing thanks to the engine.
 */
data class GameContext(val world: World,
                       val screen: Screen,
                       val uiEvent: UIEvent,
                       val player: GameEntity<Player>
) : Context