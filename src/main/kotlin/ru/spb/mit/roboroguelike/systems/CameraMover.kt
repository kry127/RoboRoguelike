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


class CameraMover: BaseFacet<GameContext>() {

    enum class CameraMovementDirection {
        FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN, STEADY
    }

    override fun executeCommand(command: Command<out EntityType, GameContext>): Response {
        return command.responseWhenCommandIs(MoveCamera::class) {(context, entity, prevPos) ->
            val world = context.world
            // Alternative:
            // world.centerCameraAtPosition(entity.position)
            when(getCameraMovementDirection(prevPos,
                                            entity.position)) {
                CameraMovementDirection.FORWARD -> world.scrollForwardBy(
                        if (entity.position.y > 15) 1 else 0
                )
                CameraMovementDirection.BACKWARD -> world.scrollBackwardBy(
                        if (entity.position.y < world.actualSize().yLength - 15) 1 else 0
                )
                CameraMovementDirection.LEFT -> world.scrollLeftBy(
                        if (entity.position.x < world.actualSize().xLength - 15) 1 else 0
                )
                CameraMovementDirection.RIGHT -> world.scrollRightBy(
                        if (entity.position.x > 30) 1 else 0
                )
                CameraMovementDirection.UP -> world.scrollOneUp()
                CameraMovementDirection.DOWN -> world.scrollOneDown()
            }
            Consumed
        }
    }

    private fun getCameraMovementDirection(prevPos: Position3D,
                                           newPos: Position3D): CameraMovementDirection {
        val (xPrev, yPrev, zPrev) = prevPos
        val (xNew, yNew, zNew) = newPos
        return when {
            yPrev < yNew -> CameraMovementDirection.FORWARD
            yPrev > yNew -> CameraMovementDirection.BACKWARD
            xPrev < xNew -> CameraMovementDirection.RIGHT
            xPrev > xNew -> CameraMovementDirection.LEFT
            zPrev < zNew -> CameraMovementDirection.UP
            zPrev > zNew -> CameraMovementDirection.DOWN
            else -> CameraMovementDirection.STEADY
        }
    }
}