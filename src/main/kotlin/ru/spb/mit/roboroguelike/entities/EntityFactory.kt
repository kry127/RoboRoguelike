package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Entities.newEntityOfType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.attributes.*
import ru.spb.mit.roboroguelike.deserialize
import ru.spb.mit.roboroguelike.objects.TileTypes
import ru.spb.mit.roboroguelike.systems.CameraMover
import ru.spb.mit.roboroguelike.systems.InputReceiver
import ru.spb.mit.roboroguelike.systems.Movable
import ru.spb.mit.roboroguelike.systems.TeleportableEntity
import ru.spb.mit.roboroguelike.systems.*
import java.io.ObjectInputStream


object EntityFactory {

    fun makePlayer() = newEntityOfType<Player, GameContext>(Player) {
        val hitpoints = EntityHitpoints(100, 100)
        val stats = EntityPrimaryStats(3, 1)
        val entityExp = EntityExperience(EntityExperience.levelToXp(1))

        entityExp.setOnLevelUpListener {
            hitpoints.onLevelUp(it)
            stats.onLevelUp(it)
        }

        attributes(EntityPosition(), EntityTile(TileTypes.PLAYER.tile),
                hitpoints, stats, entityExp
                , ConfusionSpell(0))
        behaviors(InputReceiver())
        facets(Movable(), CameraMover(), TeleportableEntity())
    }

    fun makeAggressiveMob() = newEntityOfType<AggressiveMob, GameContext>(AggressiveMob) {
        attributes(EntityPosition(), EntityTile(TileTypes.AGGRESSIVE_MOB.tile),
                EntityHitpoints(5, 5), EntityPrimaryStats(1, 1))
        behaviors(Aggressive())
        facets(Movable(), Attackable())
    }

    fun makeCowardlyMob() = newEntityOfType<CowardMob, GameContext>(CowardMob) {
        attributes(EntityPosition(), EntityTile(TileTypes.COWARDLY_MOB.tile),
                EntityHitpoints(2, 2), EntityPrimaryStats(0, 1))
        behaviors(Cowardly())
        facets(Movable(), Attackable())
    }

    fun makeStaticMob() = newEntityOfType<StaticMob, GameContext>(StaticMob) {
        attributes(EntityPosition(), EntityTile(TileTypes.STATIC_MOB.tile),
                EntityHitpoints(10, 10), EntityPrimaryStats(2, 5))
        behaviors(Static())
        facets(Attackable())
    }

    fun deserializePlayer(inputStream : ObjectInputStream) = newEntityOfType<Player, GameContext>(Player) {
        val position = Position3D.deserialize(inputStream)
        attributes(EntityPosition(position), EntityTile(TileTypes.PLAYER.tile), EntityHitpoints(100, 100))
        behaviors(InputReceiver())
        facets(Movable(), CameraMover())
    }


    fun makeLadderUp(teleportPosition: Position3D = Position3D.unknown()) = newEntityOfType<LadderUp, GameContext>(LadderUp) {
        attributes(
                EntityPosition(),
                EntityTile(TileTypes.LADDER_UP.tile),
                TeleportPosition(teleportPosition))
    }

    fun makeLadderDown(teleportPosition: Position3D = Position3D.unknown()) = newEntityOfType<LadderDown, GameContext>(LadderDown) {
        attributes(
                EntityPosition(),
                EntityTile(TileTypes.LADDER_DOWN.tile),
                TeleportPosition(teleportPosition))
    }

    // health boxes
    fun makeHealthBoxLite(healthBoxPosition: Position3D = Position3D.unknown()) = newEntityOfType<HealthBox, GameContext>(RegularHealthBox) {
        attributes(EntityPosition(), EntityTile(TileTypes.HEALTH_BOX_LITE.tile),
                EntityHitpoints(5, 5))
        facets(Consumable())
    }

    fun makeHealthBoxMedium(healthBoxPosition: Position3D = Position3D.unknown()) = newEntityOfType<HealthBox, GameContext>(RegularHealthBox) {
        attributes(EntityPosition(), EntityTile(TileTypes.HEALTH_BOX_MEDIUM.tile),
                EntityHitpoints(25, 25))
        facets(Consumable())
    }

    fun makeHealthBoxHeavy(healthBoxPosition: Position3D = Position3D.unknown()) = newEntityOfType<HealthBox, GameContext>(RegularHealthBox) {
        attributes(EntityPosition(), EntityTile(TileTypes.HEALTH_BOX_HEAVY.tile),
                EntityHitpoints(50, 50))
        facets(Consumable())
    }

    fun makeHealthBoxMega(healthBoxPosition: Position3D = Position3D.unknown()) = newEntityOfType<HealthBox, GameContext>(SuperHealthBox) {
        attributes(EntityPosition(), EntityTile(TileTypes.HEALTH_BOX_MEGA.tile),
                EntityHitpoints(100, 100))
        facets(Consumable())
    }
    // end health boxes
}