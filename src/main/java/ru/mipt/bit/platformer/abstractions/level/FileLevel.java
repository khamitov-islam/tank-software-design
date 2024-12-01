package ru.mipt.bit.platformer.abstractions.level;
import ru.mipt.bit.platformer.abstractions.controllers.AITankController;
import ru.mipt.bit.platformer.abstractions.controllers.CollisionController;
import ru.mipt.bit.platformer.abstractions.level.AbstractLevel;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import ru.mipt.bit.platformer.abstractions.controllers.GraphicsAbstraction;
import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.abstractions.handlers.TankInputHandler;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.abstractions.models.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;

public class FileLevel extends AbstractLevel implements Level{
    private final String filePath;
    private Tank playerTank;

    public FileLevel(String filePath, TileMovement tileMovement, CollisionController collisionController) {
        super(tileMovement, collisionController);
        this.filePath = filePath;
        this.playerTank = null;
    }

    @Override
    public void generate(List<BaseModel> models, TiledMapTileLayer groundLayer,
                         GraphicsAbstraction graphicsAbstraction) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); ++x) {
                    char cell = line.charAt(x);
                    GridPoint2 position = new GridPoint2(x, y);

                    switch (cell) {
                        case 'T':
                            Tree tree = new Tree("images/greenTree.png", position, groundLayer, graphicsAbstraction);
                            models.add(tree);
                            collisionController.addOccupiedPosition(position);
                            break;
                        case 'X':
                            playerTank = new Tank("images/tank_blue.png", position, 0.4f,
                                    graphicsAbstraction, new TankInputHandler());
                            models.add(playerTank);
                            collisionController.addOccupiedPosition(position);
                            break;
                    }
                }
                y++;
            }

            generateAITanks(models, graphicsAbstraction);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Tank getPlayerTank(){
        return playerTank;
    };

}


