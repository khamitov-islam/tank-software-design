package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.abstractions.controllers.AITankController;
import ru.mipt.bit.platformer.abstractions.controllers.ModelController;
import ru.mipt.bit.platformer.abstractions.level.FileLevel;
import ru.mipt.bit.platformer.abstractions.level.Level;
import ru.mipt.bit.platformer.abstractions.level.RandomLevel;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Field;
import ru.mipt.bit.platformer.abstractions.controllers.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.handlers.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;


import java.util.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;


public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private Field field;
    private List<BaseModel> models;
    private TileMovement tileMovement;
    // private InputHandler inputHandler;
    private ModelController modelController;
    private Level level;
    // private final List<AITankController> aiTankControllers;

    Config config = Config.DEFAULT;

    GameDesktopLauncher (Config config) {
        this.config = config;
        // aiTankControllers = new ArrayList<>();

    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        field = new Field("level.tmx", batch);

        TiledMapTileLayer groundLayer = field.getLayer();
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        models = new ArrayList<>();
        GraphicsAbstraction graphicsAbstraction = new GraphicsAbstraction();

        // Create appropriate level based on config
        if (config == Config.DEFAULT) {
            level = new RandomLevel(tileMovement);
        } else if (config == Config.FILE) {
            level = new FileLevel("file_loading_test/test1.txt", tileMovement);
        }

        // Generate level
        level.generate(models, groundLayer, graphicsAbstraction);

        modelController = new ModelController(models, tileMovement, graphicsAbstraction, level.getCollisionController(), level.getAiControllers());
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
