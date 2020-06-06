package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile
import ru.spb.mit.roboroguelike.attributes.EntityHitpoints
import ru.spb.mit.roboroguelike.attributes.EntityPosition
import ru.spb.mit.roboroguelike.attributes.EntityTile
import kotlin.reflect.KClass

/**
 * Здесь происходит проброс атрибутов непосредственно в Property объекта
 */

var AnyGameEntity.position
    get() = tryToFindAttribute(EntityPosition::class).position
    set(value) {
        findAttribute(EntityPosition::class).map {
            it.position = value
        }
    }

val AnyGameEntity.tile: Tile
    get() = this.tryToFindAttribute(EntityTile::class).tile

/** add HP properties **/
var AnyGameEntity.hp : Int
    get() = this.tryToFindAttribute(EntityHitpoints::class).hp
    set(value) {
        findAttribute(EntityHitpoints::class).map {
            it.hp = value
        }
    }

val AnyGameEntity.maxHp : Int
    get() = this.tryToFindAttribute(EntityHitpoints::class).maxHp



fun <T : Attribute> AnyGameEntity.tryToFindAttribute(klass: KClass<T>): T = findAttribute(klass).orElseThrow {
    NoSuchElementException("Entity '$this' has no property with type '${klass.simpleName}'.")
}