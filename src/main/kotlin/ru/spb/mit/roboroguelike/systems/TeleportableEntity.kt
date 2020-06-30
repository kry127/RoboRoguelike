package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.AttemptTeleportation
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.position

/**
 * This class represents entities that can use teleports as the way of movement (for player)
 */
class TeleportableEntity : BaseFacet<GameContext>() {
    override fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(AttemptTeleportation::class) { (context, entity) ->
            val world = context.world
            val position = entity.position
            var response: Response = Pass
            val block = world.fetchBlockAt(position)
            if (!block.isEmpty()) {
                val teleportPosition: Maybe<Position3D> = block.get().getTeleportPosition()
                if (teleportPosition.isPresent) {
                    response = entity.executeCommand(MoveTo(
                            context = context,
                            source = entity,
                            position = teleportPosition.get()
                    ))
                }
            }
            response
        }
    }

}