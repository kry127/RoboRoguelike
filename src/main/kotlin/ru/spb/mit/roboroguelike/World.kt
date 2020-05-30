
import org.hexworks.amethyst.api.Engines
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
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent
import ru.spb.mit.roboroguelike.objects.GameConfig
import java.io.ObjectOutputStream
import java.nio.file.Paths

class World(startingBlocks: Map<Position3D, GameBlock>,
            visibleSize: Size3D,
            actualSize: Size3D,
            var currentLevel: Int = GameConfig.DUNGEON_LEVELS - 1)
    : GameArea<Tile, GameBlock> by GameAreaBuilder.newBuilder<Tile, GameBlock>()
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

    fun update(screen: Screen, uiEvent: UIEvent, game: Game) {
        engine.update(GameContext(
                world = this,
                screen = screen,
                uiEvent = uiEvent,
                player = game.player))
    }

    fun centerCameraAtPosition(cameraPosition: Position3D) {
        val (xLength, yLength, _) = visibleSize()
        val (xCurr, yCurr, _) = cameraPosition
        scrollTo3DPosition(cameraPosition
                .withX(xCurr - xLength / 2)
                .withY(yCurr - yLength / 2))
    }

    fun addEntity(entity: AnyGameEntity, position: Position3D) {
        engine.addEntity(entity)
        entity.position = position
        fetchBlockAt(position).map {
            it.addEntity(entity)
        }
    }

    fun searchForEmptyRandomPosition(offset: Position3D = Positions.default3DPosition(),
                                     searchSpace: Size3D = actualSize(),
                                     n_tries: Int = 20): Maybe<Position3D> {
        val (xLength, yLength, zLength) = searchSpace
        var result = Maybe.empty<Position3D>()
        var j = 0
        while (result.isEmpty() && j < n_tries) {
            val currPos = Positions.create3DPosition(
                Random.nextInt(offset.x, offset.x + xLength),
                Random.nextInt(offset.y, offset.y + yLength),
                Random.nextInt(offset.z, offset.z + zLength))
            fetchBlockAt(currPos).map {
                if (!it.isOccupied) {
                    result = Maybe.of(currPos)
                }
            }
            j++
        }
        return result
    }

    fun addAtEmptyRandomPosition(entity: AnyGameEntity): Boolean {
        val pos = searchForEmptyRandomPosition()
        if (pos.isEmpty()) {
            return false
        }
        addEntity(entity, pos.get())
        return true
    }

    fun moveEntity(entity: AnyGameEntity, newPosition3D: Position3D): Boolean {
        val oldBlock = fetchBlockAt(entity.position)
        val newBlock = fetchBlockAt(newPosition3D)
        if (!moveIsPossible(oldBlock, newBlock)) {
            return false;
        }
        oldBlock.get().removeEntity(entity)
        newBlock.get().addEntity(entity)
        entity.position = newPosition3D
        return true
    }

    fun moveIsPossible(oldBlock: Maybe<GameBlock>,
                       newBlock: Maybe<GameBlock>): Boolean {
        return oldBlock.isPresent && newBlock.isPresent && !newBlock.get().isOccupied
    }


    fun serializeBlocks(outputStream : ObjectOutputStream) {
        val worldSize = actualSize()
        worldSize.serialize(outputStream)

        val count = fetchBlocks().count()
        outputStream.writeInt(count)
        for (block in fetchBlocks()) {
            block.component1().serialize(outputStream) // extension function at SerializationExtensions.kt
            block.component2().serialize(outputStream)
        }
        outputStream.close()
    }

    fun defaultSerializeBlocks() {
        serializeBlocks(ObjectOutputStream(Paths.get(GameConfig.SAVE_FILE_PATH).toFile().outputStream()))
    }
}