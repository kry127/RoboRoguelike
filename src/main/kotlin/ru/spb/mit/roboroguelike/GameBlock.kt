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
import java.rmi.activation.ActivationGroup

class GameBlock(private var defaultTile: Tile = TileTypes.FLOOR.tile,
                private val currentEntities: MutableList<GameEntity<EntityType>>
                = mutableListOf())
    : BlockBase<Tile>() {

    val isFloor: Boolean
        get() = defaultTile == TileTypes.FLOOR.tile

    val isWall: Boolean
        get() = defaultTile == TileTypes.WALL.tile

    val isOccupied: Boolean
        get() = !isFloor //|| currentEntities.size > 0

    val isMob: Boolean
        get() = currentEntities.find {
            entity ->
            entity.type == AggressiveMob || entity.type == StaticMob || entity.type == CowardMob
        } != null

    val isPlayer: Boolean
        get() = currentEntities.find {
            entity ->
            entity.type == Player
        } != null

   // val isMob: Boolean
   //     get() = defaultTile == TileTypes.MOB

    val entities: List<GameEntity<EntityType>>
        get() = currentEntities.toList()

    fun serialize(outputStream : ObjectOutputStream) {
        outputStream.writeBoolean(isFloor)
        outputStream.writeBoolean(isWall)
    }

    companion object {
        fun deserialize(inputStream: ObjectInputStream) : GameBlock {
            val isFloor = inputStream.readBoolean()
            val isWall = inputStream.readBoolean()
            if (isFloor) {
                return GameBlock()
            } else if (isWall) {
                return GameBlock(TileTypes.WALL.tile)
            }
            return GameBlock(TileTypes.EMPTY.tile)
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
}