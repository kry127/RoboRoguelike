/**
 * This file mostly contains entity extension, which enables you to
 * access different properties of attributes as explicit field of the entity
 */

package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.extensions.orElseThrow
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.attributes.*
import ru.spb.mit.roboroguelike.commands.AttemptTeleportation
import ru.spb.mit.roboroguelike.commands.DropArtifact
import ru.spb.mit.roboroguelike.commands.TakeArtifact
import ru.spb.mit.roboroguelike.objects.GameConfig
import kotlin.math.ceil
import kotlin.random.Random
import kotlin.reflect.KClass

/** add position properties**/

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
var AnyGameEntity.hp: Int
    get() = this.tryToFindAttribute(EntityHitpoints::class).hp
    set(value) {
        findAttribute(EntityHitpoints::class).map {
            it.hp = value
        }
    }

var AnyGameEntity.maxHp: Int
    get() = this.tryToFindAttribute(EntityHitpoints::class).maxHp
    set(value) {
        findAttribute(EntityHitpoints::class).map {
            it.maxHp = value
        }
    }

/** add Exp properties **/
var AnyGameEntity.xp: Int
    get() = this.tryToFindAttribute(EntityExperience::class).xp
    set(value) {
        findAttribute(EntityExperience::class).map {
            it.xp = value
        }
    }


/** add attack and defence properties **/
var AnyGameEntity.attack: Int
    get() = this.tryToFindAttribute(EntityPrimaryStats::class).attack
    set(value) {
        findAttribute(EntityPrimaryStats::class).map {
            it.attack = value
        }
    }
var AnyGameEntity.defence: Int
    get() = this.tryToFindAttribute(EntityPrimaryStats::class).defence
    set(value) {
        findAttribute(EntityPrimaryStats::class).map {
            it.defence = value
        }
    }

val GameEntity<Player>.artifactAttack: Int
    get() {
        var ret = 0
        val attr = this.tryToFindAttribute(EntityArtifacts::class)
        fun slotAccumulate(slot: Maybe<GameEntity<Artifact>>) {
            if (slot.isPresent && slot.get().findAttribute(EntityPrimaryStats::class).isPresent) {
                ret += slot.get().attack
            }
        }
        slotAccumulate(attr.slot1)
        slotAccumulate(attr.slot2)
        slotAccumulate(attr.slot3)
        slotAccumulate(attr.slot4)
        return ret
    }

val GameEntity<Player>.artifactDefence: Int
    get() {
        var ret = 0
        val attr = this.tryToFindAttribute(EntityArtifacts::class)
        fun slotAccumulate(slot: Maybe<GameEntity<Artifact>>) {
            if (slot.isPresent && slot.get().findAttribute(EntityPrimaryStats::class).isPresent) {
                ret += slot.get().defence
            }
        }
        slotAccumulate(attr.slot1)
        slotAccumulate(attr.slot2)
        slotAccumulate(attr.slot3)
        slotAccumulate(attr.slot4)
        return ret
    }

val GameEntity<Player>.effectiveAttack: Int
    get() = this.attack + this.artifactAttack

val GameEntity<Player>.effectiveDefence: Int
    get() = this.defence + this.artifactDefence

/** add artifacts extensions **/
fun GameEntity<Player>.freeArtifactSlotsCount(): Int {
    val attr = this.tryToFindAttribute(EntityArtifacts::class)
    return GameConfig.NUMBER_OF_ARTIFACT_SLOTS - attr.artifactCount
}

fun GameEntity<Player>.addArtifact(art: GameEntity<Artifact>) {
    val attr = this.tryToFindAttribute(EntityArtifacts::class)
    val placed = attr.emplaceArtifact(art)
    if (placed) {
        if (art.findAttribute(EntityHitpoints::class).isPresent) {
            val fraction = this.hp.toDouble() / this.maxHp
            this.maxHp += art.hp
            this.hp = ceil(this.maxHp * fraction).toInt()
        }

    }
}

fun GameEntity<Player>.removeArtifact(artifactId: Int): Maybe<GameEntity<Artifact>> {
    val attr = this.tryToFindAttribute(EntityArtifacts::class)
    val ret = attr.displaceArtifact(artifactId)
    if (ret.isPresent) {
        if (ret.get().findAttribute(EntityHitpoints::class).isPresent) {
            val fraction = this.hp.toDouble() / this.maxHp
            this.maxHp -= ret.get().hp
            this.hp = ceil(this.maxHp * fraction).toInt()
        }
    }
    return ret
}

/** add teleport properties **/
var AnyGameEntity.teleportPosition: Position3D
    get() = this.tryToFindAttribute(TeleportPosition::class).teleportPosition
    set(value) {
        findAttribute(TeleportPosition::class).map {
            it.teleportPosition = value
        }
    }

/** add confusion spell properties **/
var AnyGameEntity.confusionDuration: Int
    get() = this.tryToFindAttribute(ConfusionSpell::class).confusionDuration
    set(value) {
        findAttribute(ConfusionSpell::class).map {
            it.confusionDuration = value
        }
    }

/** add decoration of player movement decision affected by confusion spell **/
fun GameEntity<Player>.getNextPositionFromUIEvent(uie: KeyboardEvent, context: GameContext): Position3D {
    val code = if (this.confusionDuration > 0) {
        this.confusionDuration--
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
        /** artifact part **/
        KeyCode.DIGIT_1 -> this.dropArtifact(context, 1)
        KeyCode.DIGIT_2 -> this.dropArtifact(context, 2)
        KeyCode.DIGIT_3 -> this.dropArtifact(context, 3)
        KeyCode.DIGIT_4 -> this.dropArtifact(context, 4)
        KeyCode.KEY_T -> this.tryTakeArtefact(context)
        /** end of artifact part **/
        else -> {
            this.position
        }
    }
}

private fun confuse(kk: KeyCode): KeyCode {
    val cycle = listOf(KeyCode.UP, KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT, KeyCode.UP, KeyCode.RIGHT)
    val initCode = when (kk) {
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

fun AnyGameEntity.tryTakeArtefact(context: GameContext): Position3D {
    executeCommand(TakeArtifact(
            context = context,
            source = this
    ))
    return position
}


fun AnyGameEntity.dropArtifact(context: GameContext, artifactId: Int): Position3D {
    executeCommand(DropArtifact(
            context = context,
            source = this,
            artifactId = artifactId
    ))
    return position
}