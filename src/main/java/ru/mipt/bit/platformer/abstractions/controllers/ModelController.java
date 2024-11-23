package ru.mipt.bit.platformer.abstractions.controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.abstractions.Renderable;
import ru.mipt.bit.platformer.abstractions.level.Level;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.abstractions.Movable;
import ru.mipt.bit.platformer.abstractions.models.Tree;
import ru.mipt.bit.platformer.util.TileMovement;
import java.util.List;

public class ModelController {
    private final List<BaseModel> models;
    private final TileMovement tileMovement;
    private final GraphicsAbstraction graphicsAbstraction;
    private final List<AITankController> aiControllers;
    private CollisionController collisionController;

    public ModelController(List<BaseModel> models, TileMovement tileMovement,
                           GraphicsAbstraction graphicsAbstraction, CollisionController collisionController,
                           List<AITankController> aiControllers) {
        this.models = models;
        this.tileMovement = tileMovement;
        this.graphicsAbstraction = graphicsAbstraction;
        this.collisionController = collisionController;
        this.aiControllers = aiControllers;
    }

    public void updateModels(float deltaTime) {
        for (AITankController aiController: aiControllers){
            aiController.update(deltaTime);
        }
        for (BaseModel model : models) {
            if (model instanceof Tank) {
                Tank tank = (Tank) model;
                if (!tank.isAITank()){
                    tank.handleInput();
                    if(collisionController.isPositionOccupied(tank.getDestination())
                           || !(collisionController.isWithinBounds(tank.getDestination()))){
                        tank.cancelMovement();
                    } else {tank.updatePosition(tileMovement, deltaTime);}

                }

            }
        }
        collisionController.clear();
        for (BaseModel model: models){
            collisionController.addOccupiedPosition(model.getPosition());
        }
    }


    public void renderModels(Batch batch) {
        for (BaseModel model : models) {
            if (model instanceof Renderable) {
                ((Renderable) model).render(batch);
            }
        }
    }
    public void disposeModels() {
        for (BaseModel model : models) {
            model.dispose();
        }
    }
}