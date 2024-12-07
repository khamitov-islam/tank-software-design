package ru.mipt.bit.platformer.abstractions.controllers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.compression.lzma.Base;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.Renderable;
import ru.mipt.bit.platformer.abstractions.command.MoveTankCommand;
import ru.mipt.bit.platformer.abstractions.command.ToggleHealthDisplayCommand;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.level.Level;
import ru.mipt.bit.platformer.abstractions.models.*;
import ru.mipt.bit.platformer.abstractions.Movable;
import ru.mipt.bit.platformer.util.TileMovement;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class ModelController {
    // TODO: добавить поле ToggleHealth
    private final ToggleHealthDisplayCommand toggleHealthDisplayCommand;
    private final List<BaseModel> models;
    private final TileMovement tileMovement;
    private final GraphicsAbstraction graphicsAbstraction;
    private final Tank playerTank;
    private final List<AITankController> aiControllers;
    public CollisionController collisionController;

    public ModelController(List<BaseModel> models, TileMovement tileMovement,
                           GraphicsAbstraction graphicsAbstraction,
                           CollisionController collisionController,
                           Tank playerTank , List<AITankController> aiControllers,
                           ToggleHealthDisplayCommand toggleHealthDisplayCommand
    ) {
        this.models = models;
        this.tileMovement = tileMovement;
        this.graphicsAbstraction = graphicsAbstraction;
        this.collisionController = collisionController;
        this.playerTank = playerTank;
        this.aiControllers = aiControllers;
        this.toggleHealthDisplayCommand = toggleHealthDisplayCommand;
    }


    public void updateModels(float deltaTime) {
        collisionController.update(models);

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            System.out.println("pressed ..");
            toggleHealthDisplayCommand.execute();
        }

        playerTank.handleInput();
        if (collisionController.isValidMove(playerTank.getDestination())) {
            playerTank.updatePosition(tileMovement, deltaTime);
        } else {
            playerTank.cancelMovement();
        }

        for (AITankController aiController : aiControllers) {
            aiController.update(deltaTime, collisionController);
        }
    }

    public void renderModels(Batch batch) { //, boolean isHealthDisplayEnabled
        for (BaseModel model : models) {
            if (model instanceof Renderable) {
                ((Renderable) model).render(batch);

            }
        }

        if (toggleHealthDisplayCommand.isEnabled()){
            toggleHealthDisplayCommand.showHealthDisplay(); }

    }
    
    public void disposeModels() {
        for (BaseModel model : models) {
            model.dispose();
        }
    }
}