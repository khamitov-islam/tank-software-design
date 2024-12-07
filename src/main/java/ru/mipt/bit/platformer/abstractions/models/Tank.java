package ru.mipt.bit.platformer.abstractions.models;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.command.MoveTankCommand;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.Movable;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import ru.mipt.bit.platformer.abstractions.Renderable;
import ru.mipt.bit.platformer.abstractions.handlers.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.Random;

public class Tank extends BaseModel implements Movable, Renderable, Liveable {

    private final float movementSpeed;
    //private boolean isMoving;
    private GridPoint2 currentCoordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;
    private final InputHandler inputHandler;
    private int  health;


    public Tank(String texturePath, GridPoint2 initialCoordinates, float movementSpeed, GraphicsAbstraction graphicsAbstraction, InputHandler inputHandler) {
        super(texturePath, initialCoordinates, graphicsAbstraction);
        this.currentCoordinates = initialCoordinates;
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
        this.rotation = 0f;
        this.inputHandler = inputHandler;
        this.health = new Random().nextInt(21) + 80; // 80 - 100
    }

    @Override
    public void handleInput() {
        if (isReadyForNextMove()) {
            Direction direction = inputHandler.handleInput();
            if (direction != null) {
                MoveTankCommand move = new MoveTankCommand(this, direction);
                move.execute();
                //setDestination(direction);
            }
        }
    }


    public void move(Direction direction) { //move // setDestination
        if(isReadyForNextMove()) {
            this.destinationCoordinates = direction.move(this.currentCoordinates);
            this.rotation = direction.getRotation();
            this.movementProgress = 0f;
        }
    }

    public void updatePosition(TileMovement tileMovement, float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(getRectangle(), currentCoordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isReadyForNextMove()) {
            currentCoordinates = destinationCoordinates;
        }
    }

    public GridPoint2 getCurrentCoordinates() { return currentCoordinates;}

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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}