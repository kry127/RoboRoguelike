package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.position
import kotlin.math.abs

class Cowardly: BaseBehavior<GameContext>() {

    private fun isWithinRangeOf(from: Position3D, other: Position3D, radius: Int): Boolean {
        return from.isUnknown().not()
                && other.isUnknown().not()
                && from.z == other.z
                && abs(from.x - other.x) + abs(from.y - other.y) <= radius
    }

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val (world, _, uiEvent, player) = context
        val currentPos = entity.position
        val playerPos = player.position


        if (uiEvent is KeyboardEvent && currentPos != playerPos && isWithinRangeOf(playerPos, currentPos, 30)) {
            fun handle(): Position3D{
                val x = currentPos.x - playerPos.x
                val y = currentPos.y - playerPos.y
                val l = Math.sqrt(x.toDouble() * x.toDouble() + y.toDouble() * y.toDouble())
                val xNorm = x.toDouble() / l
                val yNorm = y.toDouble() / l
                if (abs(xNorm) > abs(yNorm)){
                    return currentPos.withRelativeX(if (xNorm > 0) 1 else -1)
                }else
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