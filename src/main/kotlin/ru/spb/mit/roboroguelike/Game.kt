package ru.spb.mit.roboroguelike

import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.Player
import ru.spb.mit.roboroguelike.objects.GameConfig
import java.io.ObjectOutputStream
import java.nio.file.Paths

/**
 * Game entity is used to describe game as a whole. Consists of 'world' and a 'player'.
 */
class Game(var world: World,
           val player: GameEntity<Player>) {

    companion object {

        fun create(world: World, player: GameEntity<Player>): Game =
                Game(world, player)
    }


    private fun serialize(outputStream: ObjectOutputStream) {
        player.serialize(outputStream)
        world.serializeBlocks(outputStream)
        outputStream.close()
    }

    fun defaultSerialize() {
        serialize(ObjectOutputStream(Paths.get(GameConfig.SAVE_FILE_PATH).toFile().outputStream()))
    }
}