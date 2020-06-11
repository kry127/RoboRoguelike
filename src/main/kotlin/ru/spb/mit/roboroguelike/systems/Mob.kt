package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.uievent.Processed
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.Remove
import ru.spb.mit.roboroguelike.entities.*
import ru.spb.mit.roboroguelike.view.PlayView
import kotlin.math.abs

abstract class Mob : BaseBehavior<GameContext>() {

    fun fight(entity: GameEntity<out EntityType>, player: GameEntity<Player>, context: GameContext): Boolean {
        val currentPos = entity.position
        val playerPos = player.position

        val horizontalVicinity = currentPos.x == playerPos.x && abs(currentPos.y - playerPos.y) < 2
        val verticalVicinity = currentPos.y == playerPos.y && abs(currentPos.x - playerPos.x) < 2
        val sameDungeonLevel = currentPos.z == playerPos.z


        if (sameDungeonLevel && (horizontalVicinity || verticalVicinity)) {
            //TODO fight
            if (entity.type.equals(AggressiveMob)) {
                player.hp--;
                if (player.hp <= 0) {
                    context.world.gameOver()
                }
            }
            entity.hp--;
            if (entity.hp == 0) {
                entity.executeCommand(Remove(context, entity))
                return true;
            }
            return false;
        }
        return false
    }

    fun isWithinRangeOf(from: Position3D, other: Position3D, radius: Int): Boolean {
        return from.isUnknown().not()
                && other.isUnknown().not()
                && from.z == other.z
                && abs(from.x - other.x) + abs(from.y - other.y) <= radius
    }

}