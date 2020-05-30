
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import ru.spb.mit.roboroguelike.GameBlock

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

    companion object {
        private val DEFAULT_BLOCK = BlockTypes.floor() // 8
    }
}