package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.entities.GameEntity

/**
 * This is static behaviour specialization of 'Mob' behaviour
 */
class Static : Mob() {

    override fun update(entity: GameEntity<out EntityType>, context: GameContext): Boolean {
        fight(entity, context.player, context)
        return true
    }

}