package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.commands.AttemptTeleportation
import ru.spb.mit.roboroguelike.commands.Consume

import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.*

class InputReceiver : BaseBehavior<GameContext>() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context
        val currentPos = player.position
        if (uiEvent is KeyboardEvent) {
            val newPosition: Position3D = when (uiEvent.code) {
                KeyCode.UP -> currentPos.withRelativeY(-1)
                KeyCode.LEFT -> currentPos.withRelativeX(-1)
                KeyCode.DOWN -> currentPos.withRelativeY(1)
                KeyCode.RIGHT -> currentPos.withRelativeX(1)
                KeyCode.KEY_W -> player.tryTeleportation(context)
                else -> {
                    currentPos
                }
            }
            player.executeCommand(MoveTo(context, player, newPosition))
            val renewedPos = player.position
            context.world.fetchBlockAt(renewedPos).ifPresent {
                it.entities
                        .filter{it.type == SuperHealthBox || it.type == RegularHealthBox}
                        .filterIsInstance<GameEntity<HealthBox>>()
                        .forEach{
                            hb->hb.executeCommand(Consume(context, hb, renewedPos))
                        }
            }
        }
        return true
    }

    private fun AnyGameEntity.tryTeleportation(context: GameContext): Position3D {
        executeCommand(AttemptTeleportation(
                context = context,
                source = this
        ))
        return position
    }
}