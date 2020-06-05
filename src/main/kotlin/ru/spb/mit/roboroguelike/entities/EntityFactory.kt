package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Entities.newEntityOfType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.deserialize
import ru.spb.mit.roboroguelike.objects.TileTypes
import ru.spb.mit.roboroguelike.serialize
import ru.spb.mit.roboroguelike.systems.CameraMover
import ru.spb.mit.roboroguelike.systems.InputReceiver
import ru.spb.mit.roboroguelike.systems.Movable
import ru.spb.mit.roboroguelike.systems.TeleportableEntity
import ru.spb.mit.roboroguelike.systems.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


object EntityFactory {

    fun makePlayer() = newEntityOfType<Player, GameContext>(Player) {
        attributes(EntityPosition(), EntityTile(TileTypes.PLAYER), EntityHitpoints(100, 100))
        behaviors(InputReceiver())
        facets(Movable(), CameraMover(), TeleportableEntity())
    }

    fun makeAggressiveMob() = newEntityOfType<AggressiveMob, GameContext>(AggressiveMob) {
        attributes(EntityPosition(), EntityTile(TileTypes.AGGRESSIVE_MOB), EntityHitpoints(5, 5))
        behaviors(Aggressive())
        facets(Movable(), Attackable())
    }

    fun makeCowardlyMob() = newEntityOfType<CowardMob, GameContext>(CowardMob) {
        attributes(EntityPosition(), EntityTile(TileTypes.COWARDLY_MOB), EntityHitpoints(2, 2))
        behaviors(Cowardly())
        facets(Movable(), Attackable())
    }

    fun makeStaticMob() = newEntityOfType<StaticMob, GameContext>(StaticMob) {
        attributes(EntityPosition(), EntityTile(TileTypes.STATIC_MOB), EntityHitpoints(10, 10))
        behaviors(Static())
        facets(Attackable())
    }

    fun deserializePlayer(inputStream : ObjectInputStream) = newEntityOfType<Player, GameContext>(Player) {
        val position = Position3D.deserialize(inputStream)
        attributes(EntityPosition(position), EntityTile(TileTypes.PLAYER), EntityHitpoints(100, 100))
        behaviors(InputReceiver())
        facets(Movable(), CameraMover())
    }


    fun makeLadderUp(teleportPosition: Position3D = Position3D.unknown()) = newEntityOfType<LadderUp, GameContext>(LadderUp) {
        attributes(
                EntityPosition(),
                EntityTile(TileTypes.LADDER_UP),
                TeleportPosition(teleportPosition))
    }

    fun makeLadderDown(teleportPosition: Position3D = Position3D.unknown()) = newEntityOfType<LadderDown, GameContext>(LadderDown) {
        attributes(
                EntityPosition(),
                EntityTile(TileTypes.LADDER_DOWN),
                TeleportPosition(teleportPosition))
    }
}