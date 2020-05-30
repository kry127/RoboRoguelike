package ru.spb.mit.roboroguelike

import World
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.objects.GameConfig

class Game(val world: World) {

    companion object {

        fun create(worldSize: Size3D = GameConfig.WORLD_SIZE,
                   visibleSize: Size3D = GameConfig.VISIBLE_SIZE): Game {
            val world = WorldBuilder(worldSize).makeRooms().build(worldSize)
            world.addEntity(EntityFactory.makePlayer(), Position3D.create(20, 20, 1))
            return Game(world)
        }
    }
}