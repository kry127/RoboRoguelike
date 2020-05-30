
package ru.spb.mit.roboroguelike

import World
import org.hexworks.cobalt.datatypes.extensions.orElseGet
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.objects.GameConfig
import ru.spb.mit.roboroguelike.objects.Player

class GameBuilder(val worldSize: Size3D) {

    private val visibleSize = Sizes.create3DSize(
            xLength = GameConfig.WINDOW_WIDTH,
            yLength = GameConfig.WINDOW_HEIGHT - GameConfig.LOG_AREA_HEIGHT,
            zLength = 1)

    lateinit var world : World

    fun buildGeneratedGame() : Game {
        world = WorldBuilder(worldSize)
                .makeRooms()
                .build(visibleSize = visibleSize)

        return buildGame()
    }

    fun buildSavedGame(): Game {
        world = WorldBuilder.deserializeDefault()

        // TODO find player on the map instead (build game inits new player!)
        return buildGame()
    }

    fun buildGame(): Game {

        prepareWorld()

        val player = addPlayer()

        return Game.create(
                world = world,
                player = player)
    }

    private fun prepareWorld() = also {
        world.scrollUpBy(world.actualSize().zLength)
    }

    private fun addPlayer(): GameEntity<Player> {
        var position = world.searchForEmptyRandomPosition().orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(world.currentLevel)
        val player = EntityFactory.makePlayer()
        world.addEntity(player, position)
        world.centerCameraAtPosition(position)
        return player
    }

    companion object {

        fun defaultGame() = GameBuilder(
                worldSize = GameConfig.WORLD_SIZE).buildGeneratedGame()

        fun loadGame() = GameBuilder(GameConfig.WORLD_SIZE).buildSavedGame()
    }
}