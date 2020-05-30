package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.CommandResponse
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameCommand
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.MoveCamera
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.objects.Player
import ru.spb.mit.roboroguelike.position

class Movable: BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response {
        return command.responseWhenCommandIs(MoveTo::class) { (context, entity, position) ->
            val world = context.world
            val prevPos = entity.position
            var response: Response = Pass;
            if (world.moveEntity(entity, position)) {
                response = if (entity.type == Player) {
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