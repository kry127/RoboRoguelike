package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.data.impl.Position3D
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * This extension function needed to add serialization method to Position3D object
 */
fun Position3D.serialize(outputStream: ObjectOutputStream) {
    outputStream.write(x)
    outputStream.write(y)
    outputStream.write(z)
}