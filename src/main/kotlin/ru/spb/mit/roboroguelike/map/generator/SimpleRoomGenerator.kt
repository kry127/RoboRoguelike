package ru.spb.mit.roboroguelike.map.generator

import ru.spb.mit.roboroguelike.map.BooleanWorldMap
import kotlin.random.Random

/**
 * Класс позволяет порождать карты, которые состоят из комнат, в котором просверлены
 * все четыре стены. Используйте Builder для удобной генерации объекта этого класса.
 */
class SimpleRoomGenerator(
        val height: Int,
        val width: Int,
        val room_min_size: Int = 5,
        val room_max_size: Int = 30,
        val number_of_splits: Int = 10) {

    private var rooms: MutableList<Room> = ArrayList<Room>()

    private inner class Room(
            private val x0: Int,
            private val y0: Int,
            private val x1: Int,
            private val y1: Int
    ) {


        override fun toString(): String {
            return "[$x0 $x1] x [$y0 $y1]"
        }

        private fun splitVertically(value: Int): List<Room> {
            require(value in y0..y1)
            val r1 = Room(x0, y0, x1, value)
            val r2 = Room(x0, value, x1, y1)
            return listOf(r1, r2)
        }

        private fun splitHorizontally(value: Int): List<Room> {
            require(value in x0..x1)
            val r1 = Room(x0, y0, value, y1)
            val r2 = Room(value, y0, x1, y1)
            return listOf(r1, r2)
        }

        fun canSplit(): Boolean {
            return (y1 - y0 - 2 * room_min_size > 0) || (x1 - x0 - 2 * room_min_size > 0)
        }

        fun shouldSplit(): Boolean {
            return ((y1 - y0) > room_max_size) || ((x1 - x0) > room_max_size)
        }

        fun randomSplit(): List<Room> {
            val rand: Random = Random.Default
            if (y1 - y0 > x1 - x0) {
                val ysplit = rand.nextInt(y0 + room_min_size, y1 - room_min_size)
                return splitVertically(ysplit)
            } else {
                val xsplit = rand.nextInt(x0 + room_min_size, x1 - room_min_size)
                return splitHorizontally(xsplit)
            }
        }

        // returns "already perforated" and "perforation successfull" statuses
        private fun perforateHoleLeft(arr: Array<Array<Boolean>>): Pair<Boolean, Boolean> {
            if (x0 == 0) return Pair(false, false)

            var blocked = true
            val availablePerfCenters = mutableListOf<Int>()
            for (k in y0 + 1 until y1) {
                if (!arr[x0][k]) {
                    blocked = false
                } else {
                    if (k > 2 && !arr[x0 - 1][k] && !arr[x0 - 1][k - 1] && !arr[x0 - 1][k - 2]) {
                        availablePerfCenters.add(k - 1)
                    }
                }
            }
            if (!blocked) {
                return Pair(true, false)
            }
            if (blocked && availablePerfCenters.size > 0) {
                val perfCenter = availablePerfCenters.random()
                for (k in perfCenter - 1..perfCenter + 1) {
                    arr[x0][k] = false
                }
                return Pair(false, true)
            }
            return Pair(false, false)
        }

        private fun perforateHoleRight(arr: Array<Array<Boolean>>): Pair<Boolean, Boolean> {
            if (x1 == width - 1) return Pair(false, false)

            var blocked = true
            val availablePerfCenters = mutableListOf<Int>()
            for (k in y0 + 1 until y1) {
                if (!arr[x1][k]) {
                    blocked = false
                } else {
                    if (k > 2 && !arr[x1 + 1][k] && !arr[x1 + 1][k - 1] && !arr[x1 + 1][k - 2]) {
                        availablePerfCenters.add(k - 1)
                    }
                }
            }
            if (!blocked) {
                return Pair(true, false)
            }
            if (blocked && availablePerfCenters.size > 0) {
                val perfCenter = availablePerfCenters.random()
                for (k in perfCenter - 1..perfCenter + 1) {
                    arr[x1][k] = false
                }
                return Pair(false, true)
            }
            return Pair(false, false)
        }

        private fun perforateHoleTop(arr: Array<Array<Boolean>>): Pair<Boolean, Boolean> {
            if (y1 == height - 1) return Pair(false, false)

            var blocked = true
            val availablePerfCenters = mutableListOf<Int>()
            for (k in x0 + 1 until x1) {
                if (!arr[k][y1]) {
                    blocked = false
                } else {
                    if (k > 2 && !arr[k][y1 + 1] && !arr[k - 1][y1 + 1] && !arr[k - 2][y1 + 1]) {
                        availablePerfCenters.add(k - 1)
                    }
                }
            }
            if (!blocked) {
                return Pair(true, false)
            }
            if (blocked && availablePerfCenters.size > 0) {
                val perfCenter = availablePerfCenters.random()
                for (k in perfCenter - 1..perfCenter + 1) {
                    arr[k][y1] = false
                }
                return Pair(false, true)
            }
            return Pair(false, false)
        }

        private fun perforateHoleBottom(arr: Array<Array<Boolean>>): Pair<Boolean, Boolean> {
            if (y0 == 0) return Pair(false, false)

            var blocked = true
            val availablePerfCenters = mutableListOf<Int>()
            for (k in x0 + 1 until x1) {
                if (!arr[k][y0]) {
                    blocked = false
                } else {
                    if (k > 2 && !arr[k][y0 - 1] && !arr[k - 1][y0 - 1] && !arr[k - 2][y0 - 1]) {
                        availablePerfCenters.add(k - 1)
                    }
                }
            }
            if (!blocked) {
                return Pair(true, false)
            }
            if (blocked && availablePerfCenters.size > 0) {
                val perfCenter = availablePerfCenters.random()
                for (k in perfCenter - 1..perfCenter + 1) {
                    arr[k][y0] = false
                }
                return Pair(false, true)
            }
            return Pair(false, false)
        }

        fun perforateHole(arr: Array<Array<Boolean>>) {
            perforateHoleLeft(arr)
            perforateHoleRight(arr)
            perforateHoleTop(arr)
            perforateHoleBottom(arr)
        }

        fun draw(arr: Array<Array<Boolean>>) {
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

    private fun buildRoomGraph() {
        rooms.clear()
        rooms.add(Room(0, 0, width - 1, height - 1))
        for (i in 1..number_of_splits) {
            val r = rooms.find { r -> r.canSplit() } ?: break
            rooms.remove(r)
            rooms.addAll(r.randomSplit())
        }
        while (true) {
            val r = rooms.find { r -> r.shouldSplit() } ?: break
            rooms.remove(r)
            rooms.addAll(r.randomSplit())
        }
    }


    private fun makeMapWithConfig(): BooleanWorldMap {
        var arr: Array<Array<Boolean>> = Array(width) {
            Array(height) { false }
        }
        for (r in rooms) {
            r.draw(arr)
            r.perforateHole(arr)
        }
        return BooleanWorldMap(arr)
    }

    fun nextMap(): BooleanWorldMap {
        buildRoomGraph()
        return makeMapWithConfig()
    }
}