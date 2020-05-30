package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.Entities.newEntityOfType
import ru.spb.mit.roboroguelike.objects.Player
import ru.spb.mit.roboroguelike.objects.TileTypes
import ru.spb.mit.roboroguelike.systems.CameraMover
import ru.spb.mit.roboroguelike.systems.InputReceiver
import ru.spb.mit.roboroguelike.systems.Movable


object EntityFactory {

    fun makePlayer() = newEntityOfType<Player, GameContext>(Player) {
        attributes(EntityPosition(), EntityTile(TileTypes.PLAYER))
        behaviors(InputReceiver())
        facets(Movable(), CameraMover())
    }
}