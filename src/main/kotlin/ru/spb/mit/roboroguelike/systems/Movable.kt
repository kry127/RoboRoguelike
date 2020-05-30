package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameContext

class Movable: BaseFacet<GameContext>() {
    override fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        TODO("Not yet implemented")
    }
}