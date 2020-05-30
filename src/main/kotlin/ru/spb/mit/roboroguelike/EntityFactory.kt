package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.Entities.newEntityOfType
import ru.spb.mit.roboroguelike.objects.LadderDown
import ru.spb.mit.roboroguelike.objects.LadderUp
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

    fun makeLadderUp() = newEntityOfType<LadderUp, GameContext>(LadderUp) {
        attributes(EntityPosition(), EntityTile(TileTypes.LADDER_UP))
    }

    fun makeLadderDown() = newEntityOfType<LadderDown, GameContext>(LadderDown) {
        attributes(EntityPosition(), EntityTile(TileTypes.LADDER_DOWN))
    }
}