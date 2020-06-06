package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.CommandResponse
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.MoveCamera
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.*

class Movable : BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response {
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