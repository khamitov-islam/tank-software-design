package ru.mipt.bit.platformer.abstractions.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.controllers.AITankController;
import ru.mipt.bit.platformer.abstractions.controllers.CollisionController;
import ru.mipt.bit.platformer.abstractions.controllers.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;


import static com.badlogic.gdx.math.MathUtils.random;

public abstract class AbstractLevel implements Level {
    protected TileMovement tileMovement;
    protected Tank playerTank;
    protected List<AITankController> aiControllers;
    protected final CollisionController collisionController;

    public AbstractLevel(TileMovement tileMovement, CollisionController collisionController) {
        this.tileMovement = tileMovement;
        this.collisionController = collisionController;
        this.playerTank = null;
        this.aiControllers = new ArrayList<>();
    }

    protected void generateAITanks(List<BaseModel> models, GraphicsAbstraction graphicsAbstraction) {
        int numberOfAITanks = random(3,5); //

        for (int i = 0; i < numberOfAITanks; i++) {
            GridPoint2 position = getRandomFreePosition();
            Tank aiTank = new Tank("images/tank_blue.png", position, 0.4f,
                    graphicsAbstraction, null);
            models.add(aiTank);
            this.collisionController.addOccupiedPosition(position);
            aiControllers.add(new AITankController(aiTank, tileMovement));
        }
    }

    protected GridPoint2 getRandomFreePosition() {
        GridPoint2 position;
        do {
            position = new GridPoint2(
                    random(0, getLevelWidth() - 1),
                    random(0, getLevelHeight() - 1)
            );
        } while (collisionController.isPositionOccupied(position));
        return position;
    }


    @Override
    public int getLevelHeight() {
        return tileMovement.getHeight();
    }

    @Override
    public int getLevelWidth() {
        return tileMovement.getWidth();
    }

    @Override
    public CollisionController getCollisionController(){
        return collisionController;
    }

    @Override
    public List<AITankController> getAIControllers() {
        return aiControllers;
    };
}
