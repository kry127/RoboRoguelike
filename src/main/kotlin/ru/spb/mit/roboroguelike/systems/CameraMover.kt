package ru.spb.mit.roboroguelike.systems

import org.hexworks.amethyst.api.Command
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.commands.MoveCamera
import ru.spb.mit.roboroguelike.entities.position
import ru.spb.mit.roboroguelike.objects.GameConfig


/**
 * This class defines entity that can affect the camera (can be followed)
 */
class CameraMover : BaseFacet<GameContext>() {

    /**
     * This enum defines direction of the camera movement after the input
     */
    enum class CameraMovementDirection {
        FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN, JUMP, STEADY
    }

    override fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(MoveCamera::class) { (context, entity, prevPos) ->
            val world = context.world
            // Alternative:
            when (getCameraMovementDirection(prevPos,
                    entity.position)) {
                CameraMovementDirection.JUMP -> world.centerCameraAtPosition(entity.position)
                CameraMovementDirection.FORWARD -> world.scrollForwardBy(
                        if (entity.position.y > GameConfig.VERTICAL_LUFT) 1 else 0
                )
                CameraMovementDirection.BACKWARD -> world.scrollBackwardBy(
                        if (entity.position.y < world.actualSize().yLength - GameConfig.VERTICAL_LUFT) 1 else 0
                )
                CameraMovementDirection.LEFT -> world.scrollLeftBy(
                        if (entity.position.x < world.actualSize().xLength - GameConfig.HORIZONTAL_LUFT) 1 else 0
                )
                CameraMovementDirection.RIGHT -> world.scrollRightBy(
                        if (entity.position.x > GameConfig.HORIZONTAL_LUFT) 1 else 0
                )
                CameraMovementDirection.UP -> world.scrollOneUp()
                CameraMovementDirection.DOWN -> world.scrollOneDown()
            }
            Consumed
        }
    }

    private fun getCameraMovementDirection(prevPos: Position3D,
                                           newPos: Position3D): CameraMovementDirection {
        val (xDelta, yDelta, zDelta) = prevPos - newPos
        return when {
            xDelta == 0 && yDelta == -1 && zDelta == 0 -> CameraMovementDirection.FORWARD
            xDelta == 0 && yDelta == +1 && zDelta == 0 -> CameraMovementDirection.BACKWARD
            xDelta == -1 && yDelta == 0 && zDelta == 0 -> CameraMovementDirection.RIGHT
            xDelta == +1 && yDelta == 0 && zDelta == 0 -> CameraMovementDirection.LEFT
            xDelta == 0 && yDelta == 0 && zDelta == -1 -> CameraMovementDirection.UP
            xDelta == 0 && yDelta == 0 && zDelta == +1 -> CameraMovementDirection.DOWN
            prevPos == newPos -> CameraMovementDirection.STEADY
            else -> CameraMovementDirection.JUMP
        }
    }
}