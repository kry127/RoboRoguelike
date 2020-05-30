package ru.spb.mit.roboroguelike

import World
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.objects.GameConfig

class Game(val world: World) {

    companion object {

        fun create(worldSize: Size3D = GameConfig.WORLD_SIZE,
                   visibleSize: Size3D = GameConfig.VISIBLE_SIZE) = Game(WorldBuilder(worldSize)
                .makeRooms()
                .build(visibleSize))
    }
}