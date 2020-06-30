package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.position
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * This class describes behaviour of a coward mob
 */
class Cowardly : Mob() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val (_, _, uiEvent, player) = context
        val currentPos = entity.position
        val playerPos = player.position

        fight(entity, context.player, context)
        if (uiEvent is KeyboardEvent && currentPos != playerPos && isWithinRangeOf(playerPos, currentPos, 30)) {
            fun handle(): Position3D {
                val x = currentPos.x - playerPos.x
                val y = currentPos.y - playerPos.y
                val l = sqrt(x.toDouble() * x.toDouble() + y.toDouble() * y.toDouble())
                val xNorm = x.toDouble() / l
                val yNorm = y.toDouble() / l
                if (abs(xNorm) > abs(yNorm)) {
                    return currentPos.withRelativeX(if (xNorm > 0) 1 else -1)
                } else
                    return currentPos.withRelativeY(if (yNorm > 0) 1 else -1)

            }

            val newPosition = when (uiEvent.code) {
                KeyCode.UP, KeyCode.LEFT, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.SPACE -> handle()
                else -> currentPos
            }
            entity.executeCommand(MoveTo(context, entity, newPosition))
        }
        return true
    }

}