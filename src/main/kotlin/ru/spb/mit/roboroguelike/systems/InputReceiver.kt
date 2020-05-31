package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.position

class InputReceiver : BaseBehavior<GameContext>() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context
        val currentPos = player.position
        if (uiEvent is KeyboardEvent) {
            val newPosition = when (uiEvent.code) {
                KeyCode.UP -> currentPos.withRelativeY(-1)
                KeyCode.LEFT -> currentPos.withRelativeX(-1)
                KeyCode.DOWN -> currentPos.withRelativeY(1)
                KeyCode.RIGHT -> currentPos.withRelativeX(1)
                else -> {
                    currentPos
                }
            }
            player.executeCommand(MoveTo(context, player, newPosition))
        }
        return true
    }
}