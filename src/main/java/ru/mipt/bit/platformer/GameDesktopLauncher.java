package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.config.Config;
import ru.mipt.bit.platformer.abstractions.config.GameConfig;
import org.springframework.core.env.MapPropertySource;
import java.util.HashMap;
import java.util.Map;

import ru.mipt.bit.platformer.abstractions.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.abstractions.level.*;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Field;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.command.ToggleHealthDisplayCommand;


import java.util.*;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;


public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;
    private Field field;
    private List<BaseModel> models;
    private GraphicalLevel graphicalLevel;
    private LogicalLevel logicalLevel;
    private ToggleHealthDisplayCommand toggleHealthDisplayCommand;
    private final Config gameConfig;
    private  ApplicationContext context;


    GameDesktopLauncher (Config config) {
        this.gameConfig = config;
    }

    private void initializeSpringContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        Map<String, Object> properties = new HashMap<>();
        properties.put("game.config", gameConfig);
        context.getEnvironment().getPropertySources().addFirst(
                new MapPropertySource("gameProperties", properties)
        );
        context.register(GameConfig.class);
        context.refresh();
        this.context = context;
    }

    @Override
    public void create() {

        initializeSpringContext();

        this.models = context.getBean("gameModels", List.class);
        this.field = context.getBean(Field.class);
        this.batch = context.getBean(Batch.class);

        Level level = context.getBean(Level.class);
        level.generate(models, field.getLayer(), context.getBean(GraphicsAbstraction.class));

        this.toggleHealthDisplayCommand = context.getBean(ToggleHealthDisplayCommand.class);
        this.graphicalLevel = context.getBean(GraphicalLevel.class);
        this.logicalLevel = context.getBean(LogicalLevel.class);

        logicalLevel.setPlayerTank(level.getPlayerTank());
        logicalLevel.setAIControllers(level.getAIControllers());
        logicalLevel.registerObserver(graphicalLevel);
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            toggleHealthDisplayCommand.execute();
        }

        logicalLevel.updateModels(deltaTime);

        field.render();

        batch.begin();

        graphicalLevel.renderModels(batch);

        if (toggleHealthDisplayCommand.isEnabled()){
            toggleHealthDisplayCommand.showHealthDisplay(); }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        field.dispose();
        graphicalLevel.disposeModels();
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
