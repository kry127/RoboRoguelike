package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.Consume
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.*

/**
 * This class represents behaviour of entity that responds on a key events
 */
class InputReceiver : BaseBehavior<GameContext>() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context
        if (uiEvent is KeyboardEvent) {
            val newPosition: Position3D = player.getNextPositionFromUIEvent(uiEvent, context)
            player.executeCommand(MoveTo(context, player, newPosition))
            val renewedPos = player.position
            context.world.fetchBlockAt(renewedPos).ifPresent {
                it.entities
                        .filter { it.type == SuperHealthBox || it.type == RegularHealthBox }
                        .filterIsInstance<GameEntity<HealthBox>>()
                        .forEach { hb ->
                            hb.executeCommand(Consume(context, hb, renewedPos))
                        }
            }
        }
        return true
    }
}