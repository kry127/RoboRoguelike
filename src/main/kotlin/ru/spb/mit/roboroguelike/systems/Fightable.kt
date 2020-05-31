package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.entities.GameEntity

class Fightable : BaseBehavior<GameContext>() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        //TODO add fighting when we do heal point
        return true
    }

}