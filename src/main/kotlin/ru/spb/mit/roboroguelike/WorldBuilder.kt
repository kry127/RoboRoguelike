package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D

class WorldBuilder(private val worldSize: Size3D) { // 1

    private val width = worldSize.xLength
    private val height = worldSize.zLength
    private var blocks: MutableMap<Position3D, GameBlock> = mutableMapOf() // 2

    fun makeCaves(): WorldBuilder { // 3
        return randomizeTiles()
                .smooth(8)
    }

    fun build(visibleSize: Size3D): World = World(blocks, visibleSize, worldSize) // 4

    private fun randomizeTiles(): WorldBuilder {
        forAllPositions { pos ->
            blocks[pos] = if (Math.random() < 0.5) { // 5
                BlockTypes.floor()
            } else BlockTypes.wall()
        }
        return this
    }

    private fun smooth(iterations: Int): WorldBuilder {
        val newBlocks = mutableMapOf<Position3D, GameBlock>() // 6
        repeat(iterations) {
            forAllPositions { pos ->
                val (x, y, z) = pos // 7
                var floors = 0
                var rocks = 0
                pos.sameLevelNeighborsShuffled().plus(pos).forEach { neighbor -> // 8
                    blocks.whenPresent(neighbor) { block -> // 9
                        if (block.isFloor) {
                            floors++
                        } else rocks++
                    }
                }
                newBlocks[Positions.create3DPosition(x, y, z)] = if (floors >= rocks) BlockTypes.floor() else BlockTypes.wall()
            }
            blocks = newBlocks // 10
        }
        return this
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) { // 11
        worldSize.fetchPositions().forEach(fn)
    }

    private fun MutableMap<Position3D, GameBlock>.whenPresent(pos: Position3D, fn: (GameBlock) -> Unit) { // 12
        this[pos]?.let(fn)
    }
}