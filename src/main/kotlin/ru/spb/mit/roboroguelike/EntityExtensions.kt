package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile
import ru.spb.mit.roboroguelike.objects.Player
import ru.spb.mit.roboroguelike.objects.TileTypes
import java.io.ObjectOutputStream
import kotlin.reflect.KClass

var AnyGameEntity.position
    get() = tryToFindAttribute(EntityPosition::class).position
    set(value) {
        findAttribute(EntityPosition::class).map {
            it.position = value
        }
    }

val AnyGameEntity.tile: Tile
    get() = this.tryToFindAttribute(EntityTile::class).tile


fun <T : Attribute> AnyGameEntity.tryToFindAttribute(klass: KClass<T>): T = findAttribute(klass).orElseThrow {
    NoSuchElementException("Entity '$this' has no property with type '${klass.simpleName}'.")
}

fun <T : EntityType> GameEntity<T>.serialize(outputStream : ObjectOutputStream) {
    outputStream.writeChars(this.description)
    outputStream.writeChars(this.name)
//    this.id.serialize(outputStream)
    when (this.type) {
        TileTypes.EMPTY -> outputStream.writeInt(0)
        TileTypes.FLOOR -> outputStream.writeInt(1)
        TileTypes.WALL -> outputStream.writeInt(2)
        TileTypes.PLAYER -> outputStream.writeInt(3)
    }
}