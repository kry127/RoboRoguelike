package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.Consume
import ru.spb.mit.roboroguelike.entities.*
import java.lang.Integer.max
import java.lang.Integer.min

/**
 * This class represents behaviour of entity, that it can be consumable by player
 */
class Consumable : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) =
            command.responseWhenCommandIs(Consume::class)
            { (context, source) ->
                // could be other consumable
                if (source.type == RegularHealthBox || source.type == SuperHealthBox) {
                    if (source.type == SuperHealthBox) {
                        context.player.hp += source.maxHp
                        context.player.hp = min(context.player.hp, 2 * context.player.maxHp)
                        context.world.removeEntity(source)
                    } else {
                        val healthGap = max(context.player.maxHp - context.player.hp, 0)
                        if (healthGap > 0) {
                            context.player.hp += min(source.maxHp, healthGap)
                            context.world.removeEntity(source)
                        }
                    }
                }
                Consumed
            }
}