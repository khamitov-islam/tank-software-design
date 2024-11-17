package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

import ru.mipt.bit.platformer.abstractions.ModelController;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Field;
import ru.mipt.bit.platformer.abstractions.models.Tree;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.handlers.InputHandler;
import ru.mipt.bit.platformer.abstractions.handlers.TankInputHandler;
import ru.mipt.bit.platformer.util.TileMovement;
import static com.badlogic.gdx.math.MathUtils.random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.random;


public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private Field field;
    private List<BaseModel> models;
    private TileMovement tileMovement;
    private InputHandler inputHandler;
    private ModelController modelController;
    Config config = Config.DEFAULT;

    GameDesktopLauncher (Config config) {
        this.config = config;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        field = new Field("level.tmx", batch);

        TiledMapTileLayer groundLayer = field.getLayer();
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        models = new ArrayList<>();
        GraphicsAbstraction graphicsAbstraction = new GraphicsAbstraction();

        if (config == Config.DEFAULT) {
            generateDefaultLevel(groundLayer, graphicsAbstraction);
        } else if (config == Config.FILE) {
            generateLevelFromFile("file_loading_test/test1.txt");
        }

        modelController = new ModelController(models, tileMovement, graphicsAbstraction);
    }

    private void generateDefaultLevel(TiledMapTileLayer groundLayer, GraphicsAbstraction graphicsAbstraction) {
        Set<GridPoint2> occupiedPositions = new HashSet<>();
        GridPoint2 playerPosition;
        do {
            playerPosition = new GridPoint2(random.nextInt(groundLayer.getWidth()), random.nextInt(groundLayer.getHeight()));
        } while (occupiedPositions.contains(playerPosition));
        models.add(new Tank("images/tank_blue.png", playerPosition, 0.4f, graphicsAbstraction, new TankInputHandler()));
        occupiedPositions.add(playerPosition);
        int numberOfTrees = random.nextInt(10) + 5;
        for (int i = 0; i < numberOfTrees; i++) {
            GridPoint2 treePosition;
            do {
                treePosition = new GridPoint2(random.nextInt(groundLayer.getWidth()), random.nextInt(groundLayer.getHeight()));
            } while (occupiedPositions.contains(treePosition));
            models.add(new Tree("images/greenTree.png", treePosition, groundLayer, graphicsAbstraction));
            occupiedPositions.add(treePosition);
        }
    }
    private void generateLevelFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); ++x) {
                    char cell = line.charAt(x);
                    GridPoint2 position = new GridPoint2(x, y);
                    if (cell == 'T') {
                        models.add(new Tree("images/greenTree.png", position, field.getLayer(), new GraphicsAbstraction()));
                    } else if (cell == 'X') {
                        models.add(new Tank("images/tank_blue.png", position, 0.4f, new GraphicsAbstraction(), new TankInputHandler()));
                    }
                }
                y++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        modelController.updateModels(deltaTime);

        models.sort(Comparator.comparingInt(model -> -model.getPosition().y));
        field.render();
        batch.begin();
        modelController.renderModels(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        field.dispose();
        modelController.disposeModels();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(Config.DEFAULT), config);
    }
}
