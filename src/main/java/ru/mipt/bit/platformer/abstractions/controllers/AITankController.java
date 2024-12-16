package ru.mipt.bit.platformer.abstractions.controllers;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.command.MoveTankCommand;
import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.util.TileMovement;


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
                MoveTankCommand aiMove = new MoveTankCommand(aiTank, chosenDirection);
                aiMove.execute();
                // aiTank.setDestination(chosenDirection);
                collisionController.addOccupiedPosition(nextPosition);
            } else {
                aiTank.cancelMovement();
            }
        }
        aiTank.updatePosition(tileMovement, deltaTime);
    }
    public Tank getAiTank(){
        return aiTank;
    }
}