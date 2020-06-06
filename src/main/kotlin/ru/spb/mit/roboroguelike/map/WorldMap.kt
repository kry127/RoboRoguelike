package ru.spb.mit.roboroguelike.map

open class WorldMap<T> (open val container: Array<Array<T>>) {
}

class BooleanWorldMap(override val container: Array<Array<Boolean>>) : WorldMap<Boolean>(container) {

    fun foreach(wall_coords_observer : (Int, Int) -> Unit) {
        // call observer
        for (i in container.indices) {
            for (j in container[i].indices) {
                if (container[i][j]) {
                    wall_coords_observer(i, j)
                }
            }
        }
    }
}