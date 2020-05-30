package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.data.BlockSide
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BlockBase
import ru.spb.mit.roboroguelike.objects.TileTypes

class GameBlock(private var defaultTile: Tile = TileTypes.FLOOR) // 1
    : BlockBase<Tile>() { // 2

    val isFloor: Boolean
        get() = defaultTile == TileTypes.FLOOR // 3


    val isWall: Boolean
        get() = defaultTile == TileTypes.WALL


    override val layers
        get() = mutableListOf(defaultTile) // 4


    override fun fetchSide(side: BlockSide): Tile {
        return TileTypes.EMPTY // 5
    }
}