
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Engines
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import ru.spb.mit.roboroguelike.*
import kotlin.random.Random

class World(startingBlocks: Map<Position3D, GameBlock>, // 1
            visibleSize: Size3D,
            actualSize: Size3D) : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>() // 2
        .withVisibleSize(visibleSize) // 3
        .withActualSize(actualSize) // 4
        .withDefaultBlock(DEFAULT_BLOCK) // 5
        .withLayersPerBlock(1) // 6
        .build() {

    init {
        startingBlocks.forEach { pos, block ->
            setBlockAt(pos, block) // 7
        }
    }

    private val engine = Engines.newEngine<GameContext>()

    companion object {
        private val DEFAULT_BLOCK = BlockTypes.floor()
    }

    fun addEntity(entity: AnyGameEntity, position: Position3D) {
        engine.addEntity(entity)
        entity.position = position
        fetchBlockAt(position).map {
            it.addEntity(entity)
        }
    }


/*    fun addAtEmptyPosition(entity: GameEntity<EntityType>, // 5
                           offset: Position3D = Positions.default3DPosition(),
                           size: Size3D = actualSize()): Boolean {
        return findEmptyLocationWithin(offset, size).fold(
                whenEmpty = { // 6
                    false
                },
                whenPresent = { location ->  // 7
                    addEntity(entity, location)
                    true
                })

    }*/

    fun addAtEmptyRandomPosition(entity: AnyGameEntity): Boolean {
        val pos = searchForEmptyRandomPosition()
        if (pos.isEmpty()) {
            return false
        }
        fetchBlockAt(pos.get()).map {
            it.addEntity(entity)
        }
        return true
    }
/*
    fun findEmptyLocationWithin(offset: Position3D, size: Size3D): Maybe<Position3D> { // 8
        var position = Maybe.empty<Position3D>()
        val maxTries = 10
        var currentTry = 0
        while (position.isPresent.not() && currentTry < maxTries) {
            val pos = Positions.create3DPosition(
                    x = (Math.random() * size.xLength).toInt() + offset.x,
                    y = (Math.random() * size.yLength).toInt() + offset.y,
                    z = (Math.random() * size.zLength).toInt() + offset.z)
            fetchBlockAt(pos).map {
                if (!it.isOccupied) {
                    position = Maybe.of(pos)
                }
            }
            currentTry++
        }
        return position
    }*/

    fun searchForEmptyRandomPosition(level: Int = 0,
                                     offset: Position3D = Positions.default3DPosition(),
                                     searchScope: Size3D = actualSize(),
                                     n_tries: Int = 10): Maybe<Position3D> {
        val xCoord = searchScope.xLength;
        val yCoord = searchScope.yLength;
        var result = Maybe.empty<Position3D>()
        var j = 0
        while (result.isEmpty() && j < n_tries) {
            val currPos = Positions.create3DPosition(
                Random.nextInt(offset.x, offset.x + xCoord),
                Random.nextInt(offset.y, offset.y + yCoord),
                level)
            fetchBlockAt(currPos).map {
                if (!it.isOccupied) {
                    result = Maybe.of(currPos)
                }
            }
            j++
        }
        return result
    }
}