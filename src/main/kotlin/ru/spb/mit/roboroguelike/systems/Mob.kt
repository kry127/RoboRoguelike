package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.attributes.EntityHitpoints
import ru.spb.mit.roboroguelike.attributes.EntityPrimaryStats
import ru.spb.mit.roboroguelike.commands.Remove
import ru.spb.mit.roboroguelike.entities.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.min
import kotlin.random.Random

abstract class Mob : BaseBehavior<GameContext>() {

    private fun reduceAttackByDefence(attack: Int, defence: Int) : Int {
        return ceil(attack * (1 / (1 + exp(defence.toDouble() - 5)))).toInt();
    }

    fun fight(entity: GameEntity<out EntityType>, player: GameEntity<Player>, context: GameContext): Boolean {
        val currentPos = entity.position
        val playerPos = player.position

        val horizontalVicinity = currentPos.x == playerPos.x && abs(currentPos.y - playerPos.y) < 2
        val verticalVicinity = currentPos.y == playerPos.y && abs(currentPos.x - playerPos.x) < 2
        val sameDungeonLevel = currentPos.z == playerPos.z


        if (sameDungeonLevel && (horizontalVicinity || verticalVicinity)) {
            //TODO fight
            var dropArtefact = true
            if (entity.type.equals(AggressiveMob) || entity.type.equals(StaticMob)) {
                // entities that can fight
                player.hp -= reduceAttackByDefence(entity.attack, player.effectiveDefence);
                if (player.hp <= 0) {
                    context.world.gameOver()
                }
            } else if (entity.type.equals(CowardMob)) {
                // coward is not fighting, but has a chance to confuse an enemy
                val rand = Random.nextDouble()
                if (rand < 0.05) {
                    // drop artefact chance
                    dropArtefact = true
                } else if (rand < 0.50) {
                    player.confusionDuration += 5
                } else if (rand < 0.80) {
                    player.confusionDuration += 10
                } else {
                    player.confusionDuration += 15
                }
                player.confusionDuration = min(player.confusionDuration, 45)
            }

            entity.hp -= reduceAttackByDefence(player.effectiveAttack, entity.defence);
            if (entity.hp <= 0) {
                entity.executeCommand(Remove(context, entity))
                if (dropArtefact) {
                    val rand = Random.nextDouble()
                    if (rand < 0.30) {
                        val artefact = EntityFactory.makePrimaryStatsArtefact(entity.position,
                                EntityPrimaryStats((Random.nextInt(1, 4)), 0))
                        context.world.addEntity(artefact, entity.position)
                    } else if (rand < 0.60) {
                        val artefact = EntityFactory.makePrimaryStatsArtefact(entity.position,
                                EntityPrimaryStats(0, (Random.nextInt(1, 4))))
                        context.world.addEntity(artefact, entity.position)
                    } else if (rand < 0.90) {
                        val hp = 25*(Random.nextInt(1, 4))
                        val artefact = EntityFactory.makeHealthArtefact(entity.position,
                                EntityHitpoints(hp, hp))
                        context.world.addEntity(artefact, entity.position)
                    } else if (rand < 0.95) {
                        val artefact = EntityFactory.makePrimaryStatsArtefact(entity.position,
                                EntityPrimaryStats((Random.nextInt(2, 5)), Random.nextInt(2, 5)))
                        context.world.addEntity(artefact, entity.position)
                    } else {
                        val hp = 25*(Random.nextInt(5, 8))
                        val artefact = EntityFactory.makeHealthArtefact(entity.position,
                                EntityHitpoints(hp, hp))
                        context.world.addEntity(artefact, entity.position)
                    }
                }
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