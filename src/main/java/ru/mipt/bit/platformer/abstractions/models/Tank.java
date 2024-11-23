package ru.mipt.bit.platformer.abstractions.models;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.controllers.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.Movable;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import ru.mipt.bit.platformer.abstractions.Renderable;
import ru.mipt.bit.platformer.abstractions.handlers.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;

public class Tank extends BaseModel implements Movable, Renderable {

    private final float movementSpeed;

    private GridPoint2 currentCoordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;
    private final InputHandler inputHandler;


    public Tank(String texturePath, GridPoint2 initialCoordinates, float movementSpeed, GraphicsAbstraction graphicsAbstraction, InputHandler inputHandler) {
        super(texturePath, initialCoordinates, graphicsAbstraction);
        this.currentCoordinates = initialCoordinates;
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
        this.rotation = 0f;
        this.inputHandler = inputHandler;
    }

    @Override
    public void handleInput() {
        if (isReadyForNextMove()) {
            Direction direction = inputHandler.handleInput();
            if (direction != null) {
                destinationCoordinates = direction.move(currentCoordinates);
                rotation = direction.getRotation();
                movementProgress = 0f;
            }
        }
    }

    public void updatePosition(TileMovement tileMovement, float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(getRectangle(), currentCoordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isReadyForNextMove()) {
            currentCoordinates.set(destinationCoordinates);

        }
    }

    public GridPoint2 getDestination() {
        return new GridPoint2(destinationCoordinates);
    }

    @Override
    public void render(Batch batch) {
        graphicsAbstraction.render(batch, getGraphics(), getRectangle(), rotation);
    }

    public void cancelMovement() {
        movementProgress = 1f;
    }

    public boolean isReadyForNextMove() {
        return isEqual(movementProgress, 1f);
    }

    public void setDestination(GridPoint2 destination, float rotation) {
        this.destinationCoordinates.set(destination);
        this.rotation = rotation;
        this.movementProgress = 0f;
    }
    public boolean isAITank(){
        return inputHandler == null;
    }
}