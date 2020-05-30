package ru.spb.mit.roboroguelike.map

internal open class WorldMap<T> (open val container: Array<Array<T>>) {
}

internal class BooleanWorldMap(override val container: Array<Array<Boolean>>) : WorldMap<Boolean>(container) {
    fun print() {
        for (row in container) {
            for (v in row) {
                if (v) {
                    print('#')
                } else {
                    print('.')
                }
            }
            println()
        }
    }
}