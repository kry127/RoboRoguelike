package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.objects.GameConfig

class Game(val world: World) {

    companion object {

        fun create(worldSize: Size3D = GameConfig.WORLD_SIZE,
                   visibleSize: Size3D = GameConfig.WORLD_SIZE) = Game(WorldBuilder(worldSize)
                .makeCaves()
                .build(visibleSize))
    }
}