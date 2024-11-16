package ru.mipt.bit.platformer.abstractions;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.abstractions.models.Tree;
import ru.mipt.bit.platformer.abstractions.movement.Movable;
import ru.mipt.bit.platformer.util.TileMovement;
import java.util.List;

public class ModelController {
    private final List<BaseModel> models;
    private final TileMovement tileMovement;
    private final GraphicsAbstraction graphicsAbstraction;

    public ModelController(List<BaseModel> models, TileMovement tileMovement, GraphicsAbstraction graphicsAbstraction) {
        this.models = models;
        this.tileMovement = tileMovement;
        this.graphicsAbstraction = graphicsAbstraction;
    }
    public void updateModels(float deltaTime) {
        for (BaseModel model : models) {
            if (model instanceof Movable) {
                Movable movableModel = (Movable) model;
                movableModel.handleInput();
                if (isColliding((Tank) movableModel)) {
                    movableModel.cancelMovement();
                } else {
                    movableModel.updatePosition(tileMovement, deltaTime);
                }
            }
        }
    }

    private boolean isColliding(Tank tank) {
        for (BaseModel model : models) {
            if (model instanceof Tree && model.isCollides(tank.getDestination())) {
                return true;
            }
        }
        return false;
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