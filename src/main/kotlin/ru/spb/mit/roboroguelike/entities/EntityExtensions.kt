package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.attributes.*
import ru.spb.mit.roboroguelike.commands.AttemptTeleportation
import kotlin.random.Random
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

/** add Exp properties **/
var AnyGameEntity.xp : Int
    get() = this.tryToFindAttribute(EntityExperience::class).xp
    set(value) {
        findAttribute(EntityExperience::class).map {
            it.xp = value
        }
    }


/** add attack and defence properties **/
var AnyGameEntity.attack : Int
    get() = this.tryToFindAttribute(EntityPrimaryStats::class).attack
    set(value) {
        findAttribute(EntityPrimaryStats::class).map {
            it.attack = value
        }
    }
var AnyGameEntity.defence : Int
    get() = this.tryToFindAttribute(EntityPrimaryStats::class).defence
    set(value) {
        findAttribute(EntityPrimaryStats::class).map {
            it.defence = value
        }
    }

/** add confusion spell properties **/
var AnyGameEntity.confusionDuration : Int
    get() = this.tryToFindAttribute(ConfusionSpell::class).confusionDuration
    set(value) {
        findAttribute(ConfusionSpell::class).map {
            it.confusionDuration = value
        }
    }

/** add decoration of player movement decision affected by confusion spell **/
fun GameEntity<Player>.getNextPositionFromUIEvent(uie : KeyboardEvent, context: GameContext) : Position3D {
    val code = if (this.confusionDuration > 0) {
        this.confusionDuration--;
        confuse(uie.code)// reduce confusion duration
    } else {
        uie.code
    }
    return when (code) {
        KeyCode.UP -> this.position.withRelativeY(-1)
        KeyCode.LEFT -> this.position.withRelativeX(-1)
        KeyCode.DOWN -> this.position.withRelativeY(1)
        KeyCode.RIGHT -> this.position.withRelativeX(1)
        KeyCode.KEY_W -> this.tryTeleportation(context)
        else -> {
            this.position
        }
    }
}

private fun confuse(kk : KeyCode) : KeyCode {
    val cycle = listOf(KeyCode.UP, KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT, KeyCode.UP, KeyCode.RIGHT)
    val initCode = when(kk) {
        KeyCode.UP -> 4
        KeyCode.LEFT -> 3
        KeyCode.DOWN -> 2
        KeyCode.RIGHT -> 1
        else -> return kk
    }
    return cycle[initCode + Random.nextInt(-1, 2)]
}



fun <T : Attribute> AnyGameEntity.tryToFindAttribute(klass: KClass<T>): T = findAttribute(klass).orElseThrow {
    NoSuchElementException("Entity '$this' has no property with type '${klass.simpleName}'.")
}



fun AnyGameEntity.tryTeleportation(context: GameContext): Position3D {
    executeCommand(AttemptTeleportation(
            context = context,
            source = this
    ))
    return position
}