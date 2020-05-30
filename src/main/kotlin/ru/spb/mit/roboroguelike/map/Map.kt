package ru.spb.mit.roboroguelike.map

open class Map<T> (open val container: Array<Array<T>>) {
}

class BooleanMap(override val container: Array<Array<Boolean>>) : Map<Boolean>(container) {
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