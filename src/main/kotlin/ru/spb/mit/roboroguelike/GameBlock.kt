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
        get() = defaultTile == TileTypes.FLOOR // 3

    val isWall: Boolean
        get() = defaultTile == TileTypes.WALL

    val isOccupied: Boolean
        get() = !isFloor || currentEntities.size > 0

    val entities: Iterable<GameEntity<EntityType>> // 3
        get() = currentEntities.toList()

    override val layers
        get() = mutableListOf(defaultTile) // 4

    override fun fetchSide(side: BlockSide): Tile {
        return TileTypes.EMPTY // 5
    }

    fun addEntity(entity: AnyGameEntity) {
        currentEntities.add(entity)
    }

    fun removeEntity(entity: AnyGameEntity) {
        currentEntities.remove(entity)
    }
}