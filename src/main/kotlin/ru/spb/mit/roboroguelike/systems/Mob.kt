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
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.min
import kotlin.random.Random

abstract class Mob : BaseBehavior<GameContext>() {

    fun fight(entity: GameEntity<out EntityType>, player: GameEntity<Player>, context: GameContext): Boolean {
        val currentPos = entity.position
        val playerPos = player.position

        val horizontalVicinity = currentPos.x == playerPos.x && abs(currentPos.y - playerPos.y) < 2
        val verticalVicinity = currentPos.y == playerPos.y && abs(currentPos.x - playerPos.x) < 2
        val sameDungeonLevel = currentPos.z == playerPos.z


        if (sameDungeonLevel && (horizontalVicinity || verticalVicinity)) {
            //TODO fight
            if (entity.type.equals(AggressiveMob) || entity.type.equals(StaticMob)) {
                player.hp -= ceil(entity.attack * (1/(1 + exp(player.defence.toDouble() - 15)))).toInt();
                if (player.hp <= 0) {
                    context.world.gameOver()
                }
            } else if (entity.type.equals(CowardMob)) {
                val rand= Random.nextDouble()
                if (rand < 0.65) {
                    // nothing
                } else if (rand < 0.85) {
                    player.confusionDuration += 5
                } else if (rand < 0.95) {
                    player.confusionDuration += 10
                } else {
                    player.confusionDuration += 15
                }
                player.confusionDuration = min(player.confusionDuration, 45)
            }
            entity.hp -= player.attack;
            if (entity.hp <= 0) {
                entity.executeCommand(Remove(context, entity))
                player.xp += entity.maxHp // simple XP gain
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