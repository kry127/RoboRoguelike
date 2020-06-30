package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.attributes.TeleportPosition
import ru.spb.mit.roboroguelike.entities.*
import ru.spb.mit.roboroguelike.objects.TileTypes
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * GameBlock -- is a special entity, which has unique coordinates and serves
 * as a container for entities. Movable entities can transcend game blockes and
 * move around the ru.spb.mit.roboroguelike.World.
 */
class GameBlock(private var defaultTile: Tile = TileTypes.FLOOR.tile,
                private val currentEntities: MutableList<GameEntity<EntityType>>
                = mutableListOf())
    : BlockBase<Tile>() {

    private val isFloor: Boolean
        get() = defaultTile == TileTypes.FLOOR.tile

    private val isWall: Boolean
        get() = defaultTile == TileTypes.WALL.tile

    val isOccupied: Boolean
        get() = !isFloor //|| currentEntities.size > 0

    val isMob: Boolean
        get() = currentEntities.find { entity ->
            entity.type == AggressiveMob || entity.type == StaticMob || entity.type == CowardMob
        } != null

    val isPlayer: Boolean
        get() = currentEntities.find { entity ->
            entity.type == Player
        } != null

    // val isMob: Boolean
    //     get() = defaultTile == TileTypes.MOB

    val entities: List<GameEntity<EntityType>>
        get() = currentEntities.toList()

    fun serialize(outputStream: ObjectOutputStream) {
        outputStream.writeBoolean(isFloor)
        outputStream.writeBoolean(isWall)
        // remember entities
        val entities = currentEntities.filter { it.name != Player::name.get() }
        outputStream.writeInt(entities.count())
        entities.forEach {
            it.serialize(outputStream)
        }
    }

    companion object {
        fun floor() = GameBlock(TileTypes.FLOOR.tile)

        fun wall() = GameBlock(TileTypes.WALL.tile)

        fun deserialize(inputStream: ObjectInputStream): GameBlock {
            val isFloor = inputStream.readBoolean()
            val isWall = inputStream.readBoolean()

            var gameBlock = GameBlock(TileTypes.EMPTY.tile)
            if (isFloor) {
                gameBlock = GameBlock()
            } else if (isWall) {
                gameBlock = GameBlock(TileTypes.WALL.tile)
            }

            val entitiesCount = inputStream.readInt()
            for (k in 0 until entitiesCount) {
                val entity = EntityFactory.deserialize(inputStream)
                gameBlock.addEntity(entity)
            }
            return gameBlock
        }
    }

    override val layers: MutableList<Tile>
        get() {
            val entityTiles = currentEntities.map { it.tile }
            val tile = when {
                entityTiles.contains(TileTypes.PLAYER.tile) -> TileTypes.PLAYER.tile
                entityTiles.isNotEmpty() -> entityTiles.first()
                else -> defaultTile
            }
            return mutableListOf(tile)
        }

    override fun fetchSide(side: BlockSide): Tile {
        return TileTypes.EMPTY.tile
    }

    fun addEntity(entity: AnyGameEntity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: AnyGameEntity) {
        currentEntities.remove(entity)
    }

    fun getTeleportPosition(): Maybe<Position3D> {
        var result: Position3D? = null
        for (entity in currentEntities) {
            val tmp = entity.findAttribute(TeleportPosition::class)
            if (tmp.isPresent) {
                result = tmp.get().teleportPosition
                break
            }
        }
        return Maybe.ofNullable(result)
    }


    fun getArtifact(atPosition: Position3D): Maybe<GameEntity<Artifact>> {
        return Maybe.ofNullable(
                currentEntities.filterIsInstance<GameEntity<Artifact>>()
                        .find {
                            it.position == atPosition && (
                                    it.type.name == StatsArtifact::name.get()
                                            ||
                                            it.type.name == HealthArtifact::name.get()
                                    )
                        }
        )
    }
}