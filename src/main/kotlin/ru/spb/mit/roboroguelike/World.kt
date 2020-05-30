
import org.hexworks.amethyst.api.Engine
import org.hexworks.amethyst.api.Engines
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import ru.spb.mit.roboroguelike.AnyGameEntity
import ru.spb.mit.roboroguelike.GameBlock
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.position
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