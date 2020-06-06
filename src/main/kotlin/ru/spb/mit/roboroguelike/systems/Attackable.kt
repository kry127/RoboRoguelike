package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.Remove
import ru.spb.mit.roboroguelike.entities.GameCommand

class Attackable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) = command.responseWhenCommandIs(Remove::class) { (context, source) ->
        context.world.removeEntity(source)
        Consumed
    }
}