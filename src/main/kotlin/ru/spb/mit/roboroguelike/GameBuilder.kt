
package ru.spb.mit.roboroguelike

import World
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.orElseGet
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import ru.spb.mit.roboroguelike.entities.*
import ru.spb.mit.roboroguelike.objects.GameConfig
import java.io.ObjectInputStream
import java.nio.file.Paths
import kotlin.random.Random

class GameBuilder(val worldSize: Size3D) {

    private val visibleSize = Sizes.create3DSize(
            xLength = GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH,
            yLength = GameConfig.WINDOW_HEIGHT - GameConfig.LOG_AREA_HEIGHT,
            zLength = 1)

    lateinit var world : World

    fun buildGeneratedGame() : Game {
        world = WorldBuilder(worldSize)
                .makeRooms()
                .build(visibleSize = visibleSize)

        prepareWorld()
        buildLadders()
        val player = addPlayer()
        (0..GameConfig.DUNGEON_LEVELS).forEach { level ->
            (1..200).forEach { _ ->
                addAggressiveMob(level)
                addCowardlyMob(level)
                addStaticMob(level)
                addHealthBox(level)
            }
        }
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
        var position = world.searchForEmptyRandomPosition(
                fixedZ = Maybe.of(world.currentLevel)
        ).orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(world.currentLevel)
        val player = EntityFactory.makePlayer()
        world.addEntity(player, position)
        world.centerCameraAtPosition(position)
        return player
    }

    private fun buildLadders() {
        for (levelIdx in GameConfig.DUNGEON_LEVELS - 1 downTo 1) {
            world.addLadderConnection(levelIdx, levelIdx - 1)
        }
    }

    private fun addAggressiveMob(level: Int = world.currentLevel): GameEntity<AggressiveMob> {
        var position = world.searchForEmptyRandomPosition(
            fixedZ = Maybe.of(level)
        ).orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(level)
        val mob = EntityFactory.makeAggressiveMob()
        world.addEntity(mob, position)
        return mob
    }

    private fun addCowardlyMob(level: Int = world.currentLevel): GameEntity<CowardMob> {
        var position = world.searchForEmptyRandomPosition(
                fixedZ = Maybe.of(level)
        ).orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(level)
        val mob = EntityFactory.makeCowardlyMob()
        world.addEntity(mob, position)
        return mob
    }

    private fun addStaticMob(level: Int = world.currentLevel): GameEntity<StaticMob> {
        var position = world.searchForEmptyRandomPosition(
                fixedZ = Maybe.of(level)
        ).orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(level)
        val mob = EntityFactory.makeStaticMob()
        world.addEntity(mob, position)
        return mob
    }



    private fun addHealthBox(level: Int = world.currentLevel): GameEntity<HealthBox> {
        var position = world.searchForEmptyRandomPosition(
                fixedZ = Maybe.of(level)
        ).orElseGet {
            Position3D.defaultPosition() }
        position = position.withZ(level)
        var hpBox : Entity<HealthBox, GameContext>? = null
        val rand= Random.nextDouble()
        if (rand < 0.4) {
            hpBox = EntityFactory.makeHealthBoxLite(position)
        } else if (rand < 0.7) {
            hpBox = EntityFactory.makeHealthBoxMedium(position)
        } else if (rand < 0.9) {
            hpBox = EntityFactory.makeHealthBoxHeavy(position)
        } else {
            hpBox = EntityFactory.makeHealthBoxMega(position)
        }
        world.addEntity(hpBox, position)
        return hpBox
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