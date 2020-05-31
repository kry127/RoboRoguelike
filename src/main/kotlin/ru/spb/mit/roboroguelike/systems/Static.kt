package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEvent
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.MoveTo
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.position

class Static : Mob() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        fight(entity, context.player, context)
        return true
    }

}