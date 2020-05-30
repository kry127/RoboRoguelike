package ru.spb.mit.roboroguelike

import World
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.objects.GameConfig
import ru.spb.mit.roboroguelike.objects.Player

class Game(var world: World,
           val player: GameEntity<Player>) {

    companion object {

        fun create(player: GameEntity<Player>, world: World): Game {
            world.addEntity(player, Position3D.create(20, 20, 1))
            return Game(world, player)
        }
    }
}