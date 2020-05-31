import org.hexworks.amethyst.api.Engines
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.UIEvent
import ru.spb.mit.roboroguelike.Game
import ru.spb.mit.roboroguelike.GameBlock
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.entities.AnyGameEntity
import ru.spb.mit.roboroguelike.entities.position
import ru.spb.mit.roboroguelike.objects.GameConfig
import ru.spb.mit.roboroguelike.serialize
import java.io.ObjectOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

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
        val (_, _, currZ) = visibleOffset()
        val (xCamera, yCamera, zCamera) = cameraPosition
/*      scrollTo3DPosition(cameraPosition
                .withX(xCurr - xLength / 2)
                .withY(yCurr - yLength / 2))*/
        scrollRightBy(xCamera - xLength / 2)
        scrollForwardBy(yCamera - yLength / 2)
        if (currZ < zCamera) {
            scrollUpBy(zCamera - currZ)
        } else {
            scrollDownBy(currZ - zCamera)
        }
    }

    fun getCameraCenter(): Position3D {
        val (xLength, yLength, _) = visibleSize()
        val (xOffset, yOffset, zOffset) = visibleOffset()
        return Position3D.create(
                x = xOffset + xLength / 2,
                y = yOffset + yLength / 2,
                z = zOffset)
    }

    fun removeEntity(entity: AnyGameEntity) {
        engine.removeEntity(entity)
        fetchBlockAt(entity.position).map {
            it.removeEntity(entity)
        }
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


    fun serializeBlocks(outputStream: ObjectOutputStream) {
        // this serialization is the first because of the architecture:
        val worldSize = actualSize()
        worldSize.serialize(outputStream)

        outputStream.writeInt(currentLevel)

        val count = fetchBlocks().count()
        outputStream.writeInt(count)
        for (block in fetchBlocks()) {
            block.component1().serialize(outputStream) // extension function at SerializationExtensions.kt
            block.component2().serialize(outputStream)
        }
    }

    fun findPathBetween(from: Position3D, to: Position3D): List<Position3D> {
        println("1: " + from + " --- " + to)
        val nodes: Queue<Pair<Position3D, Position3D?>> = LinkedList()
        nodes.add(Pair(from.withRelativeX(1), from))
        nodes.add(Pair(from.withRelativeX(-1), from))
        nodes.add(Pair(from.withRelativeY(1), from))
        nodes.add(Pair(from.withRelativeY(-1), from))
        val parents: MutableMap<Position3D, Position3D?> = mutableMapOf()
        parents.put(from, null)
        var i = 0
        while (nodes.isNotEmpty()) {
            i++
            val (pos, parent) = nodes.poll()
            if (parents.containsKey(pos)) continue
            parents.put(pos, parent)
            if (pos == to) break
            val blockMaybe = fetchBlockAt(pos)
            if (blockMaybe.isPresent) {
                val block = blockMaybe.get()
                if (!block.isOccupied) {
                    if (!parents.containsKey(pos.withRelativeX(1)))
                        nodes.add(Pair(pos.withRelativeX(1), pos))
                    if (!parents.containsKey(pos.withRelativeX(-1)))
                        nodes.add(Pair(pos.withRelativeX(-1), pos))
                    if (!parents.containsKey(pos.withRelativeY(1)))
                        nodes.add(Pair(pos.withRelativeY(1), pos))
                    if (!parents.containsKey(pos.withRelativeY(-1)))
                        nodes.add(Pair(pos.withRelativeY(-1), pos))
                }
            }
        }
        var parent: Position3D? = to
        val result: MutableList<Position3D> = ArrayList()
        while (parent != null && parent != from) {
            println(result.size)
            result.add(parent)
            parent = parents.get(parent)
        }
        result.reverse()
        return result
    }
}