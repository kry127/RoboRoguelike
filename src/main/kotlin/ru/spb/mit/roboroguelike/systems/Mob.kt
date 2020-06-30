package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.attributes.EntityHitpoints
import ru.spb.mit.roboroguelike.attributes.EntityPrimaryStats
import ru.spb.mit.roboroguelike.commands.Remove
import ru.spb.mit.roboroguelike.entities.*
import ru.spb.mit.roboroguelike.objects.GameConfig
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.min
import kotlin.random.Random

/**
 * This is a basic description of 'Mob' behaviour
 */
abstract class Mob : BaseBehavior<GameContext>() {

    private fun reduceAttackByDefence(attack: Int, defence: Int): Int {
        return ceil(attack * (1 / (1 + exp(defence.toDouble() - 5)))).toInt()
    }

    /**
     * This method is used to describe fight between mob and the player
     */
    fun fight(entity: GameEntity<out EntityType>, player: GameEntity<Player>, context: GameContext): Boolean {
        val currentPos = entity.position
        val playerPos = player.position

        val horizontalVicinity = currentPos.x == playerPos.x && abs(currentPos.y - playerPos.y) < 2
        val verticalVicinity = currentPos.y == playerPos.y && abs(currentPos.x - playerPos.x) < 2
        val sameDungeonLevel = currentPos.z == playerPos.z


        if (sameDungeonLevel && (horizontalVicinity || verticalVicinity)) {
            // boolean to indicate we should drop artifact
            var dropArtefact = false
            // probability distribution of artifacts for attack|defence|hp|att+def|advancedhp
            // 0 and 1 ommited on both ends
            var chanceList: Array<Double> = arrayOf(0.2, 0.4, 0.6, 0.8)
            player.hp -= reduceAttackByDefence(entity.attack, player.effectiveDefence)
            if (player.hp <= 0) {
                context.world.gameOver()
            }
            if (entity.type == AggressiveMob) {
                val rand = Random.nextDouble()
                if (rand < GameConfig.AGGRESSIVE_DROP_CHANCE) {
                    // drop artefact chance
                    dropArtefact = true
                }
                // entities that can fight
            } else if (entity.type == StaticMob) {
                val rand = Random.nextDouble()
                if (rand < GameConfig.STATIC_DROP_CHANCE) {
                    // drop artefact chance
                    dropArtefact = true
                    chanceList = arrayOf(0.03, 0.06, 0.1, 0.55)
                }
            } else if (entity.type == CowardMob) {
                // coward is not fighting, but has a chance to confuse an enemy
                val rand = Random.nextDouble()
                if (rand < GameConfig.COWARD_DROP_CHANCE) {
                    // drop artefact chance
                    dropArtefact = true
                    chanceList = arrayOf(0.3, 0.6, 0.9, 0.95)
                }

                // confusion probabilities
                when {
                    rand < 0.05 -> { } // nothing
                    rand < 0.50 -> {
                        player.confusionDuration += 5
                    }
                    rand < 0.80 -> {
                        player.confusionDuration += 10
                    }
                    else -> {
                        player.confusionDuration += 15
                    }
                }
                player.confusionDuration = min(player.confusionDuration, 45)
            }

            entity.hp -= reduceAttackByDefence(player.effectiveAttack, entity.defence)
            if (entity.hp <= 0) {
                entity.executeCommand(Remove(context, entity))
                if (dropArtefact) {
                    val rand = Random.nextDouble()
                    when {
                        rand < chanceList[0] -> {
                            val artefact = EntityFactory.makePrimaryStatsArtefact(entity.position,
                                    EntityPrimaryStats((Random.nextInt(1, 4)), 0))
                            context.world.addEntity(artefact, entity.position)
                        }
                        rand < chanceList[1] -> {
                            val artefact = EntityFactory.makePrimaryStatsArtefact(entity.position,
                                    EntityPrimaryStats(0, (Random.nextInt(1, 4))))
                            context.world.addEntity(artefact, entity.position)
                        }
                        rand < chanceList[2] -> {
                            val hp = 25 * (Random.nextInt(1, 4))
                            val artefact = EntityFactory.makeHealthArtefact(entity.position,
                                    EntityHitpoints(hp, hp))
                            context.world.addEntity(artefact, entity.position)
                        }
                        rand < chanceList[3] -> {
                            val artefact = EntityFactory.makePrimaryStatsArtefact(entity.position,
                                    EntityPrimaryStats((Random.nextInt(2, 5)), Random.nextInt(2, 5)))
                            context.world.addEntity(artefact, entity.position)
                        }
                        else -> {
                            val hp = 25 * (Random.nextInt(5, 8))
                            val artefact = EntityFactory.makeHealthArtefact(entity.position,
                                    EntityHitpoints(hp, hp))
                            context.world.addEntity(artefact, entity.position)
                        }
                    }
                }
                player.xp += entity.maxHp // simple XP gain
                return true
            }
            return false
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