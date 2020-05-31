package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.deserialize
import ru.spb.mit.roboroguelike.objects.TileTypes
import ru.spb.mit.roboroguelike.serialize
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

data class EntityTile(val tile: Tile = Tiles.empty()) : Attribute

class EntityPosition(initialPosition: Position3D = Position3D.unknown()) : Attribute {
    private val positionProperty = createPropertyFrom(initialPosition)

    var position: Position3D by positionProperty.asDelegate()
}