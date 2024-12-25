package ru.mipt.bit.platformer.abstractions.config;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.command.ToggleHealthDisplayCommand;
import ru.mipt.bit.platformer.abstractions.config.conditions.DefaultConfigCondition;
import ru.mipt.bit.platformer.abstractions.config.conditions.FileConfigCondition;
import ru.mipt.bit.platformer.abstractions.controllers.CollisionController;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.abstractions.level.*;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Field;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class GameConfig {

    @Bean
    public SpriteBatch spriteBatch() {
        return new SpriteBatch();
    }

    @Bean
    @DependsOn("spriteBatch")
    public Field gameField(SpriteBatch batch) {
        return new Field("level.tmx", batch);
    }


    @Bean
    public TileMovement tileMovement(Field field) {
        return new TileMovement(field.getLayer(), Interpolation.smooth);
    }

    @Bean
    public CollisionController collisionController(TileMovement tileMovement) {
        return new CollisionController(tileMovement);
    }

    @Bean
    public GraphicsAbstraction graphicsAbstraction() {
        return new GraphicsAbstraction();
    }

    @Bean
    @Conditional(DefaultConfigCondition.class)
    public Level defaultLevel(TileMovement tileMovement, CollisionController collisionController) {
        return new RandomLevel(tileMovement, collisionController);
    }

    @Bean
    @Conditional(FileConfigCondition.class)
    public Level fileLevel(TileMovement tileMovement, CollisionController collisionController) {
        return new FileLevel("file_loading_test/test1.txt", tileMovement, collisionController);
    }

    @Bean
    public List<BaseModel> gameModels() {
        return new ArrayList<>();
    }

    @Bean
    public LogicalLevel logicalLevel(
            List<BaseModel> models,
            TileMovement tileMovement,
            GraphicsAbstraction graphicsAbstraction,
            CollisionController collisionController) {
        return new LogicalLevel(models, tileMovement, graphicsAbstraction,
                collisionController);
    }


    @Bean
    @Lazy
    public ToggleHealthDisplayCommand toggleHealthDisplayCommand(
            @Qualifier("gameModels") List<BaseModel> models,
            SpriteBatch batch) {
        return new ToggleHealthDisplayCommand(models, batch);
    }

    @Bean
    @Lazy
    public GraphicalLevel graphicalLevel(
            GraphicsAbstraction graphicsAbstraction,
            List<BaseModel> models,
            ToggleHealthDisplayCommand toggleHealthDisplayCommand) {
        return new GraphicalLevel(graphicsAbstraction, models, toggleHealthDisplayCommand);
    }
}

