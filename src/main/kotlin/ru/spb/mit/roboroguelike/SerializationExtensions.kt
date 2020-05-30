package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
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