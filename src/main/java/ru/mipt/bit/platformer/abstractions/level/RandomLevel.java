package ru.mipt.bit.platformer.abstractions.level;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.controllers.CollisionController;
import ru.mipt.bit.platformer.abstractions.controllers.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.handlers.TankInputHandler;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.abstractions.models.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class RandomLevel extends AbstractLevel {
    private static final int DEFAULT_TREES = 10;
    private Tank playerTank;


    public RandomLevel(TileMovement tileMovement, CollisionController collisionController) {
        super(tileMovement, collisionController);
        this.playerTank = null;
    }

    @Override
    public void generate(List<BaseModel> models, TiledMapTileLayer groundLayer,
                         GraphicsAbstraction graphicsAbstraction) {
        // Создаем игрока
        GridPoint2 playerPosition = getRandomFreePosition();
        playerTank = new Tank("images/tank_blue.png", playerPosition, 0.4f,
                graphicsAbstraction, new TankInputHandler());
        models.add(playerTank);
        collisionController.addOccupiedPosition(playerPosition);

        // Создаем деревья
        for (int i = 0; i < DEFAULT_TREES; i++) {
            GridPoint2 treePosition = getRandomFreePosition();
            Tree tree = new Tree("images/greenTree.png", treePosition, groundLayer, graphicsAbstraction);
            models.add(tree);
            collisionController.addOccupiedPosition(treePosition);
        }

        // Создаем AI танки
        generateAITanks(models, graphicsAbstraction);
    }
    public Tank getPlayerTank(){
        return playerTank;
    }
}
