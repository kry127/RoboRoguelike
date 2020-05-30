package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase
import ru.spb.mit.roboroguelike.objects.TileTypes

class GameBlock(private var defaultTile: Tile = TileTypes.FLOOR,
                private val currentEntities: MutableList<GameEntity<EntityType>>
                = mutableListOf())
    : BlockBase<Tile>() {

    val isFloor: Boolean
        get() = defaultTile == TileTypes.FLOOR

    val isWall: Boolean
        get() = defaultTile == TileTypes.WALL

    val isOccupied: Boolean
        get() = !isFloor || currentEntities.size > 0

    val entities: Iterable<GameEntity<EntityType>>
        get() = currentEntities.toList()

    override val layers: MutableList<Tile>
        get() {
            val entityTiles = currentEntities.map { it.tile }
            val tile = when {
                entityTiles.contains(TileTypes.PLAYER) -> TileTypes.PLAYER
                entityTiles.isNotEmpty() -> entityTiles.first()
                else -> defaultTile
            }
            return mutableListOf(tile)
        }

    override fun fetchSide(side: BlockSide): Tile {
        return TileTypes.EMPTY
    }

    fun addEntity(entity: AnyGameEntity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: AnyGameEntity) {
        currentEntities.remove(entity)
    }
}