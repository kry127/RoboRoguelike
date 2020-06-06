package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.Consume
import ru.spb.mit.roboroguelike.commands.Remove
import ru.spb.mit.roboroguelike.entities.*
import java.lang.Integer.min

class Consumable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
        command.responseWhenCommandIs(Consume::class)
            { (context, source) ->
                context.player.hp += source.maxHp
                if (source.type != SuperHealthBox) {
                    context.player.hp = min(context.player.hp, context.player.maxHp)
                }
                context.world.removeEntity(source)
                Consumed
            }
}