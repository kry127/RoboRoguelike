package ru.spb.mit.roboroguelike.map.generator

import ru.spb.mit.roboroguelike.map.BooleanMap
import kotlin.collections.ArrayList
import kotlin.random.Random

/**
 * Класс позволяет порождать карты, которые состоят из комнат, в котором просверлены
 * все четыре стены. Используйте Builder для удобной генерации объекта этого класса.
 */
class SimpleRoomGenerator(
        val height : Int,
        val width : Int,
        val room_min_size: Int,
        val room_max_size: Int,
        val number_of_splits : Int) {

    private var rooms : MutableList<Room> = ArrayList<Room>()

    /**
     * Builder class for SimpleRoomGenerator
     */
    data class Builder(
            private var height : Int = 20,
            private var width : Int = 20,
            private var room_min_size : Int = 5,
            private var room_max_size : Int = 30,
            private var number_of_splits : Int = 10) {

        fun height(value: Int) = apply { this.height = value }
        fun width(value: Int) = apply { this.width = value }
        fun room_min_size(value: Int) = apply { this.room_min_size = value }
        fun room_max_size(value: Int) = apply { this.room_max_size = value }
        fun number_of_splits(value: Int) = apply { this.number_of_splits = value }
        fun build() = SimpleRoomGenerator(height, width, room_min_size, room_max_size, number_of_splits)
    }

    private inner class Room(
            private val x0 : Int,
            private val y0 : Int,
            private val x1 : Int,
            private val y1 : Int
    ) {
        var visited: Boolean = false
            get() = field
            set(value) { field = value}


        override fun toString(): String {
            return "[$x0 $x1] x [$y0 $y1]"
        }

        private fun splitVertically(value : Int) : List<Room> {
            require(value in y0..y1)
            val r1 = Room(x0, y0, x1, value)
            val r2 = Room(x0, value, x1, y1)
            return listOf(r1, r2)
        }

        private fun splitHorizontally(value : Int) : List<Room>  {
            require(value in x0..x1)
            val r1 = Room(x0, y0, value, y1)
            val r2 = Room(value, y0, x1, y1)
            return listOf(r1, r2)
        }

        fun canSplit(): Boolean {
            return (y1 - y0 - 2*room_min_size > 0) || (x1 - x0 - 2*room_min_size > 0)
        }

        fun shouldSplit(): Boolean {
            return ((y1 - y0) > room_max_size) || ((x1 - x0) > room_max_size)
        }

        fun randomSplit(): List<Room> {
            val rand : Random = Random.Default
            if (y1 - y0 > x1 - x0) {
                val ysplit = rand.nextInt(y0 + room_min_size, y1 - room_min_size)
                return splitVertically(ysplit)
            } else {
                val xsplit = rand.nextInt(x0 + room_min_size, x1 - room_min_size)
                return splitHorizontally(xsplit)
            }
        }

        // returns "already perforated" and "perforation successfull" statuses
        private fun perforateHoleLeft(arr : Array<Array<Boolean>>) : Pair<Boolean, Boolean> {
            if (x0 == 0) return Pair(false, false);

            var blocked = true
            var perfCenter = -1
            for (k in y0+1..y1-1) {
                if (!arr[x0][k]) {
                    blocked = false
                } else {
                    if (k > 2 && !arr[x0 - 1][k] && !arr[x0 - 1][k - 1] && !arr[x0 - 1][k - 2]) {
                        perfCenter = k - 1
                    }
                }
            }
            if (!blocked) {
                return Pair(true, false)
            }
            if (blocked && perfCenter != -1) {
                for (k in perfCenter - 1..perfCenter + 1) {
                    arr[x0][k] = false
                }
                return Pair(false, true)
            }
            return Pair(false, false)
        }

        private fun perforateHoleRight(arr : Array<Array<Boolean>>) : Pair<Boolean, Boolean> {
            if (x1 == width - 1) return Pair(false, false);

            var blocked = true
            var perfCenter = -1
            for (k in y0+1..y1-1) {
                if (!arr[x1][k]) {
                    blocked = false
                } else {
                    if (k > 2 && !arr[x1 + 1][k] && !arr[x1 + 1][k - 1] && !arr[x1 + 1][k - 2]) {
                        perfCenter = k - 1
                    }
                }
            }
            if (!blocked) {
                return Pair(true, false)
            }
            if (blocked && perfCenter != -1) {
                for (k in perfCenter - 1..perfCenter + 1) {
                    arr[x1][k] = false
                }
                return Pair(false, true)
            }
            return Pair(false, false)
        }

        private fun perforateHoleTop(arr : Array<Array<Boolean>>) : Pair<Boolean, Boolean> {
            if (y1 == height - 1) return Pair(false, false);

            var blocked = true
            var perfCenter = -1
            for (k in x0+1..x1-1) {
                if (!arr[k][y1]) {
                    blocked = false
                } else {
                    if (k > 2 && !arr[k][y1 + 1] && !arr[k - 1][y1 + 1] && !arr[k - 2][y1 + 1]) {
                        perfCenter = k - 1
                    }
                }
            }
            if (!blocked) {
                return Pair(true, false)
            }
            if (blocked && perfCenter != -1) {
                for (k in perfCenter - 1..perfCenter + 1) {
                    arr[k][y1] = false
                }
                return Pair(false, true)
            }
            return Pair(false, false)
        }

        private fun perforateHoleBottom(arr : Array<Array<Boolean>>) : Pair<Boolean, Boolean> {
            if (y0 == 0) return Pair(false, false);

            var blocked = true
            var perfCenter = -1
            for (k in x0+1..x1-1) {
                if (!arr[k][y0]) {
                    blocked = false
                } else {
                    if (k > 2 && !arr[k][y0 - 1] && !arr[k - 1][y0 - 1] && !arr[k - 2][y0 - 1]) {
                        perfCenter = k - 1
                    }
                }
            }
            if (!blocked) {
                return Pair(true, false)
            }
            if (blocked && perfCenter != -1) {
                for (k in perfCenter - 1..perfCenter + 1) {
                    arr[k][y0] = false
                }
                return Pair(false, true)
            }
            return Pair(false, false)
        }

        fun perforateHole(arr : Array<Array<Boolean>>) {
            val (perforated1, successfull1) = perforateHoleLeft(arr)
//            if (successfull1) return
            val (perforated2, successfull2) = perforateHoleRight(arr)
//            if (successfull2) return
            val (perforated3, successfull3) = perforateHoleTop(arr)
//            if (successfull3) return
            val (perforated4, successfull4) = perforateHoleBottom(arr)
//            if (successfull4) return
        }

        fun draw(arr : Array<Array<Boolean>>) {
            val rand : Random = Random.Default
            for (k in y0..y1) {
                arr[x0][k] = true
            }
            for (k in y0..y1) {
                arr[x1][k] = true
            }
            for (k in x0..x1) {
                arr[k][y0] = true
            }
            for (k in x0..x1) {
                arr[k][y1] = true
            }
        }
    }

    private fun debug_print_rooms() {
        for (r in rooms) {
            println(r)
        }
    }

    private fun debug_draw_rooms() {
        var arr : Array<Array<Boolean>> = Array(width) {
            Array<Boolean>(height) {false}
        }
        for (r in rooms) {
            r.draw(arr)
        }
        BooleanMap(arr).print();
    }

    private fun build_room_graph() {
        rooms.clear()
        rooms.add(Room(0, 0, width - 1, height - 1))
        for (i in 1..number_of_splits) {
            val r = rooms.find { r -> r.canSplit()} ?: break
            rooms.remove(r)
            rooms.addAll(r.randomSplit())
//            debug_print_rooms();
//            debug_draw_rooms();
//            println("===============================")
        }
        while(true) {
            val r = rooms.find { r -> r.shouldSplit()} ?: break
            rooms.remove(r)
            rooms.addAll(r.randomSplit())
        }
    }


    private fun makeMapWithConfig() : BooleanMap {
        var arr : Array<Array<Boolean>> = Array(width) {
            Array<Boolean>(height) {false}
        }
        for (r in rooms) {
            r.visited = false
        }
        for (r in rooms) {
            r.draw(arr)
            r.perforateHole(arr)
//            BooleanMap(arr).print();
        }
        return BooleanMap(arr)
    }

    fun nextMap() : BooleanMap {
        build_room_graph()
        return makeMapWithConfig()
    }
}