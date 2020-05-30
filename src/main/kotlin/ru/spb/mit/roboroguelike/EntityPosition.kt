package ru.spb.mit.roboroguelike

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D

data class EntityTile(val tile: Tile = Tiles.empty()) : Attribute

class EntityPosition(initialPosition: Position3D = Position3D.unknown()) : Attribute { // 1
    private val positionProperty = createPropertyFrom(initialPosition) // 2

    var position: Position3D by positionProperty.asDelegate() // 3
}