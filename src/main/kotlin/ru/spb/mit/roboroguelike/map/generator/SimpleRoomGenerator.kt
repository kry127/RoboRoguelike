package ru.spb.mit.roboroguelike.map.generator

import org.hexworks.zircon.internal.util.DefaultThreadSafeQueue
import ru.spb.mit.roboroguelike.map.Map
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class SimpleRoomGenerator(
        val height : Int,
        val width : Int,
        val room_min_size: Int,
        val room_max_size: Int,
        val number_of_splits : Int,
        val extra_hordes_count : Int) {

    private var rooms : MutableList<Room> = ArrayList<Room>()

    /**
     * Builder class for SimpleRoomGenerator
     */
    data class Builder(
            private var height : Int = 20,
            private var width : Int = 20,
            private var room_min_size : Int = 5,
            private var room_max_size : Int = 30,
            private var number_of_splits : Int = 10,
            private var extra_hordes_count : Int = 0) {

        fun height(value: Int) = apply { this.height = value }
        fun width(value: Int) = apply { this.width = value }
        fun room_min_size(value: Int) = apply { this.room_min_size = value }
        fun room_max_size(value: Int) = apply { this.room_max_size = value }
        fun number_of_splits(value: Int) = apply { this.number_of_splits = value }
        fun extra_hordes_count(value: Int) = apply { this.extra_hordes_count = value }
        fun build() = SimpleRoomGenerator(height, width, room_min_size, room_max_size, number_of_splits, extra_hordes_count)
    }

    private inner class Room(
            var left : Room?,
            var right: Room?,
            var top: Room?,
            var bottom: Room?,
            private val x0 : Int,
            private val y0 : Int,
            private val x1 : Int,
            private val y1 : Int
    ) {
        var visited: Boolean = false
            get() = field
            set(value) { field = value}

        var leftConnected : Room? = null
        var rightConnected: Room? = null
        var topConnected: Room? = null
        var bottomConnected: Room? = null

        private fun splitVertically(value : Int) : List<Room> {
            require(value in y0..y1)
            val r1 = Room(left, right, top, null, x0, y0, x1, value)
            val r2 = Room(left, right, null, bottom, x0, value, x1, y1)
            r1.bottom = r2
            r2.top = r1
            return listOf(r1, r2)
        }

        private fun splitHorizontally(value : Int) : List<Room>  {
            require(value in y0..y1)
            val r1 = Room(left, null, top, bottom, x0, y0, value, y1)
            val r2 = Room(null, right, top, bottom, value, y0, x1, y1)
            r1.right = r2
            r2.left = r1
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
                val xsplit = rand.nextInt(y0 + room_min_size, y1 - room_min_size)
                return splitHorizontally(xsplit)
            }
        }

        fun draw(arr : Array<Array<Boolean>>) {
            val rand : Random = Random.Default
            if (leftConnected == null) {
                for (k in y0..y1) {
                    arr[x0][k] = true
                }
            } else {
                if (visited) {
                    for (k in y0..y1) {
                        arr[x0][k] = arr[x0 - 1][k]
                    }
                } else {
                    val y_gap = rand.nextInt(y0+3, y1 - 3)
                    for (k in y0..y_gap - 2) {
                        arr[x0][k] = true
                    }
                    for (k in y_gap + 2..y1) {
                        arr[x0][k] = true
                    }
                }
            }
            if (rightConnected == null) {
                for (k in y0..y1) {
                    arr[x1][k] = true
                }
            } else {
                if (visited) {
                    for (k in y0..y1) {
                        arr[x1][k] = arr[x1 + 1][k]
                    }
                } else {
                    val y_gap = rand.nextInt(y0+3, y1 - 3)
                    for (k in y0..y_gap - 2) {
                        arr[x1][k] = true
                    }
                    for (k in y_gap + 2..y1) {
                        arr[x1][k] = true
                    }
                }
            }
            if (topConnected == null) {
                for (k in x0..x1) {
                    arr[k][y1] = true
                }
            } else {
                if (visited) {
                    for (k in x0..x1) {
                        arr[k][y1] = arr[k][y1 + 1]
                    }
                } else {
                    val x_gap = rand.nextInt(x0+3, x1 - 3)
                    for (k in x0..x_gap - 2) {
                        arr[k][y1] = true
                    }
                    for (k in x_gap + 2..x1) {
                        arr[k][y1] = true
                    }
                }
            }
            if (bottomConnected == null) {
                for (k in x0..x1) {
                    arr[k][y0] = true
                }
            } else {
                if (visited) {
                    for (k in x0..x1) {
                        arr[k][y0] = arr[k][y0 - 1]
                    }
                } else {
                    val x_gap = rand.nextInt(x0+3, x1 - 3)
                    for (k in x0..x_gap - 2) {
                        arr[k][y0] = true
                    }
                    for (k in x_gap + 2..x1) {
                        arr[k][y0] = true
                    }
                }
            }
        }
    }

    private fun build_room_graph() {
        rooms.clear()
        rooms.add(Room(null, null, null, null, 0, 0, width, height))
        for (i in 1..number_of_splits) {
            val r = rooms.find { r -> r.canSplit()} ?: break
            rooms.remove(r)
            rooms.addAll(r.randomSplit())
        }
        while(true) {
            val r = rooms.find { r -> r.shouldSplit()} ?: break
            rooms.remove(r)
            rooms.addAll(r.randomSplit())
        }
    }

    private fun bfs_spanning_tree() {
        var front : DefaultThreadSafeQueue<Room> = DefaultThreadSafeQueue<Room>()
        var room = rooms.random()
        front.add(room)
        room.visited = true

        while (front.size > 0) {
            room = front.first()
            if (room.left != null && !room.left!!.visited) {
                front.add(room.left!!)
                room.left!!.visited = true
                room.leftConnected = room.left
                room.left!!.rightConnected = room
            }
            if (room.right != null && !room.right!!.visited) {
                front.add(room.right!!)
                room.right!!.visited = true
                room.rightConnected = room.right
                room.right!!.leftConnected = room
            }
            if (room.top != null && !room.top!!.visited) {
                front.add(room.top!!)
                room.top!!.visited = true
                room.topConnected = room.top
                room.top!!.bottomConnected = room
            }
            if (room.bottom != null && !room.bottom!!.visited) {
                front.add(room.bottom!!)
                room.bottom!!.visited = true
                room.bottomConnected = room.bottom
                room.bottom!!.topConnected = room
            }
        }
    }


    private fun makeMapWithConfig() : Map<Boolean> {
        var arr : Array<Array<Boolean>> = Array(width) {
            Array<Boolean>(height) {false}
        }
        for (r in rooms) {
            r.visited = false
        }
        for (r in rooms) {
            r.draw(arr)
            r.visited = true
        }
        return Map<Boolean>(arr)
    }

    fun nextMap() : Map<Boolean> {
        build_room_graph()
        bfs_spanning_tree()
        return makeMapWithConfig()
    }
}