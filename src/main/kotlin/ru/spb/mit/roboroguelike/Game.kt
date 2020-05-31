package ru.spb.mit.roboroguelike

import World
import ru.spb.mit.roboroguelike.entities.EntityFactory
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.Player
import ru.spb.mit.roboroguelike.entities.position
import ru.spb.mit.roboroguelike.objects.GameConfig
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.file.Paths
import javax.swing.text.html.parser.Entity

class Game(var world: World,
           val player: GameEntity<Player>) {

    companion object {

        fun create(world: World, player: GameEntity<Player>): Game =
                Game(world, player)
    }


    fun serialize(outputStream : ObjectOutputStream) {
        player.serialize(outputStream)
        world.serializeBlocks(outputStream)
        outputStream.close()
    }

    fun defaultSerialize() {
        serialize(ObjectOutputStream(Paths.get(GameConfig.SAVE_FILE_PATH).toFile().outputStream()))
    }
}