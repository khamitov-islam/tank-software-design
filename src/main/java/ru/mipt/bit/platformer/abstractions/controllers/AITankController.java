package ru.mipt.bit.platformer.abstractions.controllers;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class AITankController {
    private final Tank tank;
    private final TileMovement tileMovement;
    private final CollisionController collisionController;


    public AITankController(Tank tank, TileMovement tileMovement, CollisionController collisionController ){
        this.tank = tank;
        this.tileMovement = tileMovement;
        this.collisionController = collisionController;
    }

    public void update(float deltaTime) {
        if (tank.isReadyForNextMove()) {
            List<Direction> availableDirections = getAvailableDirections();
            if(availableDirections.isEmpty()){
                tank.cancelMovement();
            } else {
                Direction nextDirection = availableDirections.get(random.nextInt(availableDirections.size()));
                GridPoint2 nextPosition = nextDirection.move(tank.getPosition());
                tank.setDestination(nextPosition, nextDirection.getRotation());
                collisionController.addOccupiedPosition(nextPosition);
            }
        }
        tank.updatePosition(tileMovement, deltaTime);
    }

    private List<Direction> getAvailableDirections() {
        List<Direction> availableDirections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (collisionController.canMove(tank, direction)) {
                availableDirections.add(direction);
            }
        }
        return availableDirections;
    }
}