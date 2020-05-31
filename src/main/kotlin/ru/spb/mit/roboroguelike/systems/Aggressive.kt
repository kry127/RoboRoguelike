package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.commands.Remove
import ru.spb.mit.roboroguelike.entities.position
import kotlin.math.abs

class Aggressive: BaseBehavior<GameContext>() {

    private fun isWithinRangeOf(from: Position3D, other: Position3D, radius: Int): Boolean {
        return from.isUnknown().not()
                && other.isUnknown().not()
                && from.z == other.z
                && abs(from.x - other.x) + abs(from.y - other.y) <= radius
    }

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val (world, screen, uiEvent, player) = context
        val currentPos = entity.position
        val playerPos = player.position

        //TODO fix
        if(currentPos.x == playerPos.x && abs(currentPos.y - playerPos.y) < 2 ||
                currentPos.y == playerPos.y && abs(currentPos.x - playerPos.x) < 2){
            entity.executeCommand(Remove(context, entity))
            return true
        }

        if (uiEvent is KeyboardEvent && currentPos != playerPos && isWithinRangeOf(playerPos, currentPos, 30)) {
            fun handle(): Position3D{
                val res = world.findPathBetween(currentPos, playerPos)
                return res.first()
            }
            val newPosition = when (uiEvent.code) {
                KeyCode.UP, KeyCode.LEFT, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.SPACE -> handle()
                else -> {
                    currentPos
                }
            }
            entity.executeCommand(MoveTo(context, entity, newPosition))
        }
        return true
    }

}