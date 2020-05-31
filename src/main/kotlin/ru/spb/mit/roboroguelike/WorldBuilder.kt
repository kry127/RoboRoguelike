package ru.spb.mit.roboroguelike

import World
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.map.generator.SimpleRoomGenerator
import ru.spb.mit.roboroguelike.objects.GameConfig
import java.io.ObjectInputStream
import java.nio.file.Paths

class WorldBuilder(private val worldSize: Size3D) {

    private val width = worldSize.xLength
    private val height = worldSize.yLength
    private val depth = worldSize.zLength
    private var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf()

    companion object {
        fun deserializeDefault() : World {
            return deserializeBlocks(ObjectInputStream(Paths.get(GameConfig.SAVE_FILE_PATH).toFile().inputStream()))
        }

        fun deserializeBlocks(inputStream: ObjectInputStream) : World {
            val worldSize = Size3D.deserialize(inputStream)
            return WorldBuilder(worldSize).deserializeBlocks(inputStream)
        }
    }



    fun deserializeBlocks(inputStream: ObjectInputStream) : World {
        val currentLevel = inputStream.readInt()
        val count = inputStream.readInt()
        for (k in 0 until count) {
//            println("Loaded ${k + 1} out of $count")
            val pos = Position3D.deserialize(inputStream)
            val gameBlock = GameBlock.deserialize(inputStream)
            blocks[pos] = gameBlock
        }
        return World(blocks, GameConfig.VISIBLE_SIZE, worldSize, currentLevel)
    }

    fun makeRooms(): WorldBuilder {
        return generateRooms()
    }

    fun build(visibleSize: Size3D): World = World(blocks, visibleSize, worldSize)

    private fun generateRooms(): WorldBuilder {
        val builder = SimpleRoomGenerator.Builder()
        val roomGenerator = builder
                .height(height)
                .width(width)
                .room_min_size(7)
                .build()
        val maps : ArrayList<Array<Array<Boolean>>> = arrayListOf();
        for (i in 1..depth) {
            maps.add(roomGenerator.nextMap().container)
        }
        forAllPositions { pos ->
            blocks[pos] = if (maps[pos.z][pos.x][pos.y]) {
                BlockTypes.wall()
            } else BlockTypes.floor()
        }
        return this
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        worldSize.fetchPositions().forEach(fn)
    }

    private fun MutableMap<Position3D, GameBlock>.whenPresent(pos: Position3D, fn: (GameBlock) -> Unit) {
        this[pos]?.let(fn)
    }
}