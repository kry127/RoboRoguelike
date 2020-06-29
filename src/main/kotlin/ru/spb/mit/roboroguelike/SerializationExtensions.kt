package ru.spb.mit.roboroguelike

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.attributes.EntityArtifacts
import ru.spb.mit.roboroguelike.attributes.EntityHitpoints
import ru.spb.mit.roboroguelike.attributes.EntityPrimaryStats
import ru.spb.mit.roboroguelike.entities.*
import ru.spb.mit.roboroguelike.objects.TileTypes
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * This extension function needed to add serialization method to Position3D object
 */
fun Position3D.serialize(outputStream: ObjectOutputStream) {
    outputStream.writeInt(x)
    outputStream.writeInt(y)
    outputStream.writeInt(z)
}


fun Position3D.Companion.deserialize(inputStream: ObjectInputStream) : Position3D{
    val x = inputStream.readInt()
    val y = inputStream.readInt()
    val z = inputStream.readInt()
    return Position3D.create(x, y, z)
}

fun Size3D.serialize(outputStream: ObjectOutputStream) {
    outputStream.writeInt(xLength)
    outputStream.writeInt(yLength)
    outputStream.writeInt(zLength)
}



fun Size3D.Companion.deserialize(inputStream: ObjectInputStream) : Size3D {
    val x = inputStream.readInt()
    val y = inputStream.readInt()
    val z = inputStream.readInt()
    return Size3D.create(x, y, z)
}

fun serializeSlot(outputStream : ObjectOutputStream, slot : Maybe<GameEntity<Artifact>>) {
    outputStream.writeBoolean(slot.isPresent)
    if (slot.isPresent) {
        outputStream.writeUTF(slot.get().name)
        slot.get().position.serialize(outputStream)
        when (slot.get().name) {
            StatsArtifact.name -> {
                outputStream.writeInt(slot.get().attack)
                outputStream.writeInt(slot.get().defence)
            }
            HealthArtifact.name -> {
                outputStream.writeInt(slot.get().hp)
            }
        }
    }
}


fun deserializeSlot(inputStream : ObjectInputStream) : Maybe<GameEntity<Artifact>>{
    val persist = inputStream.readBoolean()
    if (persist) {
        val name = inputStream.readUTF()
        val position = Position3D.deserialize(inputStream)
        when (name) {
            StatsArtifact.name -> {
                val attack = inputStream.readInt()
                val defence = inputStream.readInt()
                val stats = EntityPrimaryStats(attack, defence)
                return Maybe.of(EntityFactory.makePrimaryStatsArtefact(position, stats))
            }
            HealthArtifact.name -> {
                val hp = inputStream.readInt()
                val stats = EntityHitpoints(hp, hp)
                return Maybe.of(EntityFactory.makeHealthArtefact(position, stats))
            }
        }
    }

    return Maybe.empty()
}

// other game entities serialization
fun AnyGameEntity.serialize(outputStream : ObjectOutputStream) {
    outputStream.writeUTF(this.name)
    this.position.serialize(outputStream)
    if (Player::class.isInstance(this.type)) {
        outputStream.writeInt(this.maxHp)
        outputStream.writeInt(this.hp)
        outputStream.writeInt(this.attack)
        outputStream.writeInt(this.defence)
        outputStream.writeInt(this.xp)
        outputStream.writeInt(this.confusionDuration)
        val attr = this.findAttribute(EntityArtifacts::class)
        serializeSlot(outputStream, attr.get().slot1)
        serializeSlot(outputStream, attr.get().slot2)
        serializeSlot(outputStream, attr.get().slot3)
        serializeSlot(outputStream, attr.get().slot4)
    } else if (AggressiveMob::class.isInstance(this.type) || CowardMob::class.isInstance(this.type)
            || StaticMob::class.isInstance(this.type)) {
        outputStream.writeInt(this.maxHp)
        outputStream.writeInt(this.hp)
        outputStream.writeInt(this.attack)
        outputStream.writeInt(this.defence)
    } else if (LadderUp::class.isInstance(this.type) || LadderDown::class.isInstance(this.type)) {
        this.teleportPosition.serialize(outputStream)
    } else if (HealthBox::class.isInstance(this.type)) {
        if (this.tile == TileTypes.HEALTH_BOX_LITE.tile) {
            outputStream.writeInt(0)
        } else if (this.tile == TileTypes.HEALTH_BOX_MEDIUM.tile) {
            outputStream.writeInt(1)
        } else if (this.tile == TileTypes.HEALTH_BOX_HEAVY.tile) {
            outputStream.writeInt(2)
        } else if (this.tile == TileTypes.HEALTH_BOX_MEGA.tile) {
            outputStream.writeInt(3)
        } else {
            outputStream.writeInt(-1)
        }
        outputStream.writeInt(this.hp)
    } else if ( HealthArtifact::class.isInstance(this.type)) {
        outputStream.writeInt(this.hp)
    } else if (StatsArtifact::class.isInstance(this.type)) {
        outputStream.writeInt(this.attack)
        outputStream.writeInt(this.defence)
    }
}