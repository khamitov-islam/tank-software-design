package ru.mipt.bit.platformer.abstractions.models;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.command.MoveTankCommand;
import ru.mipt.bit.platformer.abstractions.command.ShootCommand;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.Movable;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import ru.mipt.bit.platformer.abstractions.Renderable;
import ru.mipt.bit.platformer.abstractions.handlers.InputHandler;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.Random;

public class Tank extends BaseModel implements Movable, Renderable, Liveable {
    private static final float SHOOT_COOLDOWN = 1.0f; // 1 секунда между выстрелами
    private final float movementSpeed;
    private final InputHandler inputHandler;
    private float currentCooldown = 0;
    private GridPoint2 currentCoordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;
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

    // @Override
    public void handleInput() {
        if (isReadyForNextMove()) {
            Direction direction = inputHandler.handleMovingInput();
            if (direction != null) {
                MoveTankCommand move = new MoveTankCommand(this, direction);
                move.execute();
            }
        }
//        if (canShoot()){
//            if (inputHandler.handleShootingInput())
//            {
//                ShootCommand shot = new ShootCommand(this);
//                shot.execute();
//            }
//        }

    }
    @Override
    public void render(Batch batch) {
        graphicsAbstraction.render(batch, getGraphics(), getRectangle(), rotation);
    }

    @Override
    public void move(Direction direction) { //move // setDestination
        if(isReadyForNextMove()) {
            this.destinationCoordinates = direction.move(this.currentCoordinates);
            this.rotation = direction.getRotation();
            this.movementProgress = 0f;
        }
    }

    @Override
    public void updatePosition(TileMovement tileMovement, float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(getRectangle(), currentCoordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isReadyForNextMove()) {
            currentCoordinates = destinationCoordinates;
        }
    }

    public Bullet shoot() {
        if (canShoot()) {
            GridPoint2 tankPosition = currentCoordinates;
            Direction shootDirection = Direction.getDirection(rotation);
            GridPoint2 bulletPosition = shootDirection.move(tankPosition);

            currentCooldown = SHOOT_COOLDOWN;
            // System.out.println("Bullet was created by tank with rotation: " + rotation);
            return new Bullet(bulletPosition, rotation, graphicsAbstraction, this);
        }
        return null;
    }

    public void cancelMovement() {
        movementProgress = 1f;
    }

    public boolean isReadyForNextMove() {
        return isEqual(movementProgress, 1f);
    }

    public boolean canShoot() {
        return currentCooldown <= 0;
    }

    public void updateCooldown(float deltaTime) {
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public GridPoint2 getCurrentCoordinates() { return currentCoordinates;}

    public GridPoint2 getDestination() {
        return new GridPoint2(destinationCoordinates);
    }



}