package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.position

class Aggressive : Mob() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        val (world, screen, uiEvent, player) = context
        val currentPos = entity.position
        val playerPos = player.position

        fight(entity, context.player, context)
        if (uiEvent is KeyboardEvent && currentPos != playerPos && isWithinRangeOf(playerPos, currentPos, 30)) {
            fun handle(): Position3D {
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