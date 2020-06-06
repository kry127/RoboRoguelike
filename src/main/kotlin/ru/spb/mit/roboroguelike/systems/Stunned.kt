package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.*
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.MoveCamera
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.*

class Stunned(val startTime: Long)  : BaseFacet<GameContext>() {


    override fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(MoveTo::class) { (context, entity, position) ->
            val world = context.world
            val prevPos = entity.position
            var response: Response = Pass;
            if (world.moveEntity(entity, position)) {
                response = if (entity.type == Player) {
                    if (context.player.hp > context.player.maxHp) {
                        context.player.hp--; // remove extra hp every step
                    }
                    CommandResponse(MoveCamera(
                            context = context,
                            source = entity,
                            position = prevPos
                    ))
                } else {
                    Consumed
                }
            }
            response
        }
    }
}