package ru.spb.mit.roboroguelike.map

open class WorldMap<T>(open val container: Array<Array<T>>)

class BooleanWorldMap(override val container: Array<Array<Boolean>>) : WorldMap<Boolean>(container)