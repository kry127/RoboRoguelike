
package ru.spb.mit.roboroguelike

import World
import org.hexworks.cobalt.datatypes.extensions.orElseGet
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.entities.EntityFactory
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.objects.GameConfig
import ru.spb.mit.roboroguelike.entities.Player
import ru.spb.mit.roboroguelike.entities.position
import java.io.ObjectInputStream
import java.nio.file.Paths

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

        prepareWorld()


        val player = addPlayer()
        (1..200).forEach({
            addAggressiveMob()
            addCowardlyMob()
            addStaticMob()
        })
        return Game.create(
                world = world,
                player = player)
    }

    fun buildLoadedGame(world : World, player : GameEntity<Player>): Game {
        this.world = world
        prepareWorld()
        world.addEntity(player, player.position)
        world.centerCameraAtPosition(player.position)
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

    private fun addAggressiveMob(): GameEntity<Player> {
        var position = world.searchForEmptyRandomPosition().orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(world.currentLevel)
        val mob = EntityFactory.makeAggressiveMob()
        world.addEntity(mob, position)
        return mob
    }

    private fun addCowardlyMob(): GameEntity<Player> {
        var position = world.searchForEmptyRandomPosition().orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(world.currentLevel)
        val mob = EntityFactory.makeCowardlyMob()
        world.addEntity(mob, position)
        return mob
    }

    private fun addStaticMob(): GameEntity<Player> {
        var position = world.searchForEmptyRandomPosition().orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(world.currentLevel)
        val mob = EntityFactory.makeStaticMob()
        world.addEntity(mob, position)
        return mob
    }


    companion object {

        fun defaultGame() = GameBuilder(
                worldSize = GameConfig.WORLD_SIZE).buildGeneratedGame()

        fun loadGame() = deserialize(ObjectInputStream(Paths.get(GameConfig.SAVE_FILE_PATH).toFile().inputStream()))


        fun deserialize(inputStream: ObjectInputStream) : Game {
            val player = EntityFactory.deserializePlayer(inputStream)
            val world = WorldBuilder.deserializeBlocks(inputStream)
            inputStream.close()
//            world.scrollTo3DPosition(player.position)
            return GameBuilder(GameConfig.WORLD_SIZE).buildLoadedGame(world, player)
        }
    }
}