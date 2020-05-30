
package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.objects.GameConfig
import ru.spb.mit.roboroguelike.objects.Player

class GameBuilder(val worldSize: Size3D) {

    private val visibleSize = Sizes.create3DSize(
            xLength = GameConfig.WINDOW_WIDTH,
            yLength = GameConfig.WINDOW_HEIGHT - GameConfig.LOG_AREA_HEIGHT,
            zLength = 1)

    val world = WorldBuilder(worldSize)
            .makeRooms()
            .build(visibleSize = visibleSize)

    fun buildGame(): Game {

        prepareWorld()

        val player = addPlayer()

        return Game.create(
                player = player,
                world = world)
    }

    private fun prepareWorld() = also {
        world.scrollUpBy(world.actualSize().zLength)
    }

    private fun addPlayer(): GameEntity<Player> {
        val player = EntityFactory.makePlayer()
        world.addAtEmptyRandomPosition(player)
        return player
    }

    companion object {

        fun defaultGame() = GameBuilder(
                worldSize = GameConfig.WORLD_SIZE).buildGame()
    }
}