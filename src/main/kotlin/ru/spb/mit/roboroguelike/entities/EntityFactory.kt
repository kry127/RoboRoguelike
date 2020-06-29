package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Entities.newEntityOfType
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.attributes.*
import ru.spb.mit.roboroguelike.deserialize
import ru.spb.mit.roboroguelike.deserializeSlot
import ru.spb.mit.roboroguelike.objects.TileTypes
import ru.spb.mit.roboroguelike.systems.CameraMover
import ru.spb.mit.roboroguelike.systems.InputReceiver
import ru.spb.mit.roboroguelike.systems.Movable
import ru.spb.mit.roboroguelike.systems.TeleportableEntity
import ru.spb.mit.roboroguelike.systems.*
import java.io.ObjectInputStream
import java.lang.RuntimeException


object EntityFactory {

    fun makePlayer() = newEntityOfType<Player, GameContext>(Player) {
        val hitpoints = EntityHitpoints(100, 100)
        val stats = EntityPrimaryStats(1, 1)
        val entityExp = EntityExperience(EntityExperience.levelToXp(1))

        entityExp.setOnLevelUpListener {
            hitpoints.onLevelUp(it)
            stats.onLevelUp(it)
        }

        attributes(EntityPosition(), EntityTile(TileTypes.PLAYER.tile),
                hitpoints, stats, entityExp
                , ConfusionSpell(0), EntityArtifacts())
        behaviors(InputReceiver())
        facets(Movable(), CameraMover(), TeleportableEntity(), ArtifactTaker())
    }

    fun makeAggressiveMob() = newEntityOfType<AggressiveMob, GameContext>(AggressiveMob) {
        val hitpoints = EntityHitpoints(7, 7)
        val stats = EntityPrimaryStats(1, 1)

        attributes(EntityPosition(), EntityTile(TileTypes.AGGRESSIVE_MOB.tile),
                hitpoints, stats)
        behaviors(Aggressive())
        facets(Movable(), Attackable())
    }

    fun makeCowardlyMob() = newEntityOfType<CowardMob, GameContext>(CowardMob) {
        val hitpoints = EntityHitpoints(2, 2)
        val stats = EntityPrimaryStats(0, 1)

        attributes(EntityPosition(), EntityTile(TileTypes.COWARDLY_MOB.tile),
                hitpoints, stats)
        behaviors(Cowardly())
        facets(Movable(), Attackable())
    }

    fun makeStaticMob() = newEntityOfType<StaticMob, GameContext>(StaticMob) {
        val hitpoints = EntityHitpoints(10, 10)
        val stats = EntityPrimaryStats(2, 5)

        attributes(EntityPosition(), EntityTile(TileTypes.STATIC_MOB.tile),
                hitpoints, stats)
        behaviors(Static())
        facets(Attackable())
    }

    // teleports
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

    // make artefacts
    fun makePrimaryStatsArtefact(healthBoxPosition: Position3D = Position3D.unknown(), stats: EntityPrimaryStats) = newEntityOfType<Artifact, GameContext>(StatsArtifact) {
        attributes(EntityPosition(), EntityTile(TileTypes.ARTIFACT.tile),
                stats)
    }

    fun makeHealthArtefact(healthBoxPosition: Position3D = Position3D.unknown(), hp: EntityHitpoints) = newEntityOfType<Artifact, GameContext>(HealthArtifact) {
        attributes(EntityPosition(), EntityTile(TileTypes.ARTIFACT.tile),
                hp)
    }

    fun deserialize(inputStream : ObjectInputStream) : AnyGameEntity {
        val name = inputStream.readUTF()
        val position = Position3D.deserialize(inputStream)
        return when (name) {
            Player::name.get() -> newEntityOfType<Player, GameContext>(Player) {
                val maxHp = inputStream.readInt()
                val hp = inputStream.readInt()
                val attack = inputStream.readInt()
                val defence = inputStream.readInt()
                val experience = inputStream.readInt()
                val confusion = inputStream.readInt()

                val hitpoints = EntityHitpoints(maxHp, hp)
                val stats = EntityPrimaryStats(attack, defence)
                val entityExp = EntityExperience(experience)

                entityExp.setOnLevelUpListener {
                    hitpoints.onLevelUp(it)
                    stats.onLevelUp(it)
                }

                val artifacts = EntityArtifacts()
                deserializeSlot(inputStream).map {artifacts.emplaceArtifact(it)}
                deserializeSlot(inputStream).map {artifacts.emplaceArtifact(it)}
                deserializeSlot(inputStream).map {artifacts.emplaceArtifact(it)}
                deserializeSlot(inputStream).map {artifacts.emplaceArtifact(it)}

                attributes(EntityPosition(position), EntityTile(TileTypes.PLAYER.tile),
                        hitpoints, stats, entityExp
                        , ConfusionSpell(confusion), artifacts)
                behaviors(InputReceiver())
                facets(Movable(), CameraMover(), TeleportableEntity(), ArtifactTaker())
            }
            AggressiveMob::name.get() -> newEntityOfType<AggressiveMob, GameContext>(AggressiveMob) {
                val maxHp = inputStream.readInt()
                val hp = inputStream.readInt()
                val attack = inputStream.readInt()
                val defence = inputStream.readInt()

                val hitpoints = EntityHitpoints(maxHp, hp)
                val stats = EntityPrimaryStats(attack, defence)

                attributes(EntityPosition(position), EntityTile(TileTypes.AGGRESSIVE_MOB.tile),
                        hitpoints, stats)
                behaviors(Aggressive())
                facets(Movable(), Attackable())
            }
            CowardMob::name.get() -> newEntityOfType<CowardMob, GameContext>(CowardMob) {
                val maxHp = inputStream.readInt()
                val hp = inputStream.readInt()
                val attack = inputStream.readInt()
                val defence = inputStream.readInt()

                val hitpoints = EntityHitpoints(maxHp, hp)
                val stats = EntityPrimaryStats(attack, defence)

                attributes(EntityPosition(position), EntityTile(TileTypes.COWARDLY_MOB.tile),
                        hitpoints, stats)
                behaviors(Cowardly())
                facets(Movable(), Attackable())
            }
            StaticMob::name.get() -> newEntityOfType<StaticMob, GameContext>(StaticMob) {
                val maxHp = inputStream.readInt()
                val hp = inputStream.readInt()
                val attack = inputStream.readInt()
                val defence = inputStream.readInt()

                val hitpoints = EntityHitpoints(maxHp, hp)
                val stats = EntityPrimaryStats(attack, defence)

                attributes(EntityPosition(position), EntityTile(TileTypes.STATIC_MOB.tile),
                        hitpoints, stats)
                behaviors(Static())
                facets(Movable(), Attackable())
            }
            LadderUp::name.get() -> newEntityOfType<LadderUp, GameContext>(LadderUp) {
                val teleportPosition = Position3D.deserialize(inputStream)

                attributes(
                        EntityPosition(position),
                        EntityTile(TileTypes.LADDER_UP.tile),
                        TeleportPosition(teleportPosition))
            }
            LadderDown::name.get() -> newEntityOfType<LadderDown, GameContext>(LadderDown) {
                val teleportPosition = Position3D.deserialize(inputStream)

                attributes(
                        EntityPosition(position),
                        EntityTile(TileTypes.LADDER_UP.tile),
                        TeleportPosition(teleportPosition))
            }
            RegularHealthBox::name.get() -> newEntityOfType<HealthBox, GameContext>(RegularHealthBox) {
                val tileType = inputStream.readInt()
                val hp = inputStream.readInt()

                val tile = when (tileType) {
                    0 -> TileTypes.HEALTH_BOX_LITE
                    1 -> TileTypes.HEALTH_BOX_MEDIUM
                    2 -> TileTypes.HEALTH_BOX_HEAVY
                    else -> TileTypes.HEALTH_BOX_LITE
                }

                attributes(EntityPosition(position), EntityTile(tile.tile),
                        EntityHitpoints(hp, hp))
                facets(Consumable())
            }
            SuperHealthBox::name.get() -> newEntityOfType<HealthBox, GameContext>(SuperHealthBox) {
                val tileType = inputStream.readInt()
                val hp = inputStream.readInt()

                attributes(EntityPosition(position), EntityTile(TileTypes.HEALTH_BOX_MEGA.tile),
                        EntityHitpoints(hp, hp))
                facets(Consumable())
            }
            HealthArtifact::name.get() -> newEntityOfType<Artifact, GameContext>(HealthArtifact) {
                val hp = inputStream.readInt()

                attributes(EntityPosition(position), EntityTile(TileTypes.ARTIFACT.tile),
                        EntityHitpoints(hp, hp))
            }
            StatsArtifact::name.get() -> newEntityOfType<Artifact, GameContext>(StatsArtifact) {
                val attack = inputStream.readInt()
                val defence = inputStream.readInt()

                attributes(EntityPosition(position), EntityTile(TileTypes.ARTIFACT.tile),
                        EntityPrimaryStats(attack, defence))
            }
            else -> newEntityOfType<UselessEntity, GameContext>(UselessEntity) {
                attributes(EntityPosition(position))
            }

        }

    }


    fun deserializePlayer(inputStream : ObjectInputStream) = newEntityOfType<Player, GameContext>(Player) {
        val position = Position3D.deserialize(inputStream)
        val maxHp = inputStream.readInt()
        val hp = inputStream.readInt()
        val attack = inputStream.readInt()
        val defence = inputStream.readInt()
        val experience = inputStream.readInt()
        val confusion = inputStream.readInt()

        val hitpoints = EntityHitpoints(maxHp, hp)
        val stats = EntityPrimaryStats(attack, defence)
        val entityExp = EntityExperience(experience)

        entityExp.setOnLevelUpListener {
            hitpoints.onLevelUp(it)
            stats.onLevelUp(it)
        }

        val artifacts = EntityArtifacts()
        deserializeSlot(inputStream).map {artifacts.emplaceArtifact(it)}
        deserializeSlot(inputStream).map {artifacts.emplaceArtifact(it)}
        deserializeSlot(inputStream).map {artifacts.emplaceArtifact(it)}
        deserializeSlot(inputStream).map {artifacts.emplaceArtifact(it)}

        attributes(EntityPosition(position), EntityTile(TileTypes.PLAYER.tile),
                hitpoints, stats, entityExp
                , ConfusionSpell(confusion), artifacts)
        behaviors(InputReceiver())
        facets(Movable(), CameraMover(), TeleportableEntity(), ArtifactTaker())
    }
}