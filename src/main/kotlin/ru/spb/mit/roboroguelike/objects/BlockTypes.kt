import ru.spb.mit.roboroguelike.GameBlock
import ru.spb.mit.roboroguelike.objects.TileTypes

object BlockTypes {

    fun floor() = GameBlock(TileTypes.FLOOR.tile)

    fun wall() = GameBlock(TileTypes.WALL.tile)

}