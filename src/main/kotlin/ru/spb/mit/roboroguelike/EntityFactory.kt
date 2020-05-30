package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.Entities
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.objects.Player
import ru.spb.mit.roboroguelike.objects.TileTypes
import ru.spb.mit.roboroguelike.systems.InputReceiver

fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
        Entities.newEntityOfType(type, init)

object EntityFactory {

    fun makePlayer() = newGameEntityOfType(Player) {
        attributes(EntityPosition(), EntityTile(TileTypes.PLAYER))
        behaviors(InputReceiver())
        facets()
    }
}