package ru.spb.mit.roboroguelike

import World
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.map.generator.SimpleRoomGenerator

class WorldBuilder(private val worldSize: Size3D) { // 1

    private val width = worldSize.xLength
    private val height = worldSize.zLength
    private var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf() // 2

    fun makeRooms(): WorldBuilder { // 3
        return generateRooms()
    }

    fun build(visibleSize: Size3D): World = World(blocks, visibleSize, worldSize) // 4

    private fun generateRooms(): WorldBuilder {
        val builder = SimpleRoomGenerator.Builder()
        val roomGenerator = builder.height(worldSize.yLength).width(worldSize.xLength)
                .room_min_size(7).build()
        val maps : ArrayList<Array<Array<Boolean>>> = arrayListOf();
        for (i in 1..height) {
            maps.add(roomGenerator.nextMap().container)
        }
        forAllPositions { pos ->
            blocks[pos] = if (maps[pos.z][pos.x][pos.y]) { // 5
                BlockTypes.wall()
            } else BlockTypes.floor()
        }
        return this
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) { // 11
        worldSize.fetchPositions().forEach(fn)
    }

    private fun MutableMap<Position3D, GameBlock>.whenPresent(pos: Position3D, fn: (GameBlock) -> Unit) { // 12
        this[pos]?.let(fn)
    }

    fun deserialize() : World {

    }
}