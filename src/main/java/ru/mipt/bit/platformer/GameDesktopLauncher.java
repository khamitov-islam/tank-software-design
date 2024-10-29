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

import ru.mipt.bit.platformer.abstractions.Field;
import ru.mipt.bit.platformer.abstractions.Tree;
import ru.mipt.bit.platformer.abstractions.Tank;

import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private Field field;
    private Tank tank;
    private Tree tree;
    private TileMovement tileMovement;

    @Override
    public void create() {
        batch = new SpriteBatch();
        field = new Field("level.tmx", batch);

        TiledMapTileLayer groundLayer = field.getLayer();
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        tank = new Tank("images/tank_blue.png", new GridPoint2(1, 1), 0.4f);
        tree = new Tree("images/greenTree.png", new GridPoint2(1, 3), groundLayer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        tank.handleInput();
        if (!tree.collidesWith(tank.getDestination())) {
            tank.updatePosition(tileMovement, deltaTime);
        }
        field.render();

        batch.begin();
        tank.render(batch);
        tree.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        field.dispose();
        tank.dispose();
        tree.dispose();
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
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
