package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Pass
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.*
import ru.spb.mit.roboroguelike.entities.*

class ArtifactTaker: BaseFacet<GameContext>() {
    override fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        if (TakeArtifact::class.isInstance(command)) {
            return command.responseWhenCommandIs(TakeArtifact::class) { (context, entity) ->
                val world = context.world
                val position = entity.position
                val player = context.player
                var response: Response = Pass
                val block = world.fetchBlockAt(position)
                if (!block.isEmpty()) {
                    val artifactEntity: Maybe<GameEntity<Artifact>> = block.get().getArtifact(player.position)
                    if (artifactEntity.isPresent) {
                        // check player have free slots for artifacts
                        if (player.freeArtifactSlotsCount() > 0) {
                            response = entity.executeCommand(Remove(context, artifactEntity.get()))
                            world.removeEntity(artifactEntity.get())
                            player.addArtifact(artifactEntity.get())
                        }
                    }
                }
                response
            }
        } else {
            return command.responseWhenCommandIs(DropArtifact::class) { (context, entity, artifactId) ->
                val world = context.world
                val position = entity.position
                val player = context.player
                val artifactEntity = player.removeArtifact(artifactId)
                if (artifactEntity.isPresent) {
                    world.addEntity(artifactEntity.get(), player.position)
                }
                Pass
            }
        }

    }

}