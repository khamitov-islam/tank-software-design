package ru.mipt.bit.platformer.abstractions.controllers;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.util.TileMovement;
import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.random;

public class AITankController {
    private final Tank aiTank;
    private final TileMovement tileMovement;

    public AITankController(Tank aiTank, TileMovement tileMovement){
        this.aiTank = aiTank;
        this.tileMovement = tileMovement;

    }

    public void update(float deltaTime, CollisionController collisionController) {
        if (aiTank.isReadyForNextMove()) {
            Direction chosenDirection = collisionController.getNextDirection(aiTank);
            if (chosenDirection != null){
                GridPoint2 nextPosition = chosenDirection.move(aiTank.getCurrentCoordinates());
                aiTank.setDestination(chosenDirection);
                collisionController.addOccupiedPosition(nextPosition);
            } else {
                aiTank.cancelMovement();
            }
        }
        aiTank.updatePosition(tileMovement, deltaTime);
    }

    public Tank getTank(){
        return aiTank;
    }
}