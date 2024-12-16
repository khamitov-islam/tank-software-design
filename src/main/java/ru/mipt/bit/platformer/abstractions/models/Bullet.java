package ru.mipt.bit.platformer.abstractions.models;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.GridPoint2;
import org.lwjgl.system.CallbackI;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.Movable;
import ru.mipt.bit.platformer.abstractions.Renderable;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Bullet extends BaseModel implements Renderable, Movable {
    private static final int DAMAGE = 30;
    private static final float BULLET_SPEED = 0.4f; // Быстрее чем танк
    private final float rotation;
    private GridPoint2 currentCoordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private final Tank shooter;
    // todo: добавить поле shooter(танк, который выстрелил)


    public Bullet(GridPoint2 position, float rotation, GraphicsAbstraction graphicsAbstraction, Tank shooter) {
        super("images/bullet.png", position, graphicsAbstraction); // Нужна текстура для пули
        this.rotation = rotation;
        this.currentCoordinates = position;
        this.destinationCoordinates = new GridPoint2(position);
        this.shooter = shooter;
        // System.out.println("Bullet was created at" + currentCoordinates + " with rotation " + rotation);
    }

    @Override
    public void move(Direction direction){
        if(isReadyForNextMove()) {
            this.destinationCoordinates = direction.move(this.currentCoordinates);
            this.movementProgress = 0f;
        }
        // System.out.println("Currnet coordinates: " + currentCoordinates + " and destination " + destinationCoordinates);
    }

    @Override
    public void updatePosition(TileMovement tileMovement, float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(getRectangle(), currentCoordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, BULLET_SPEED);

        if (isReadyForNextMove()) {
            currentCoordinates = destinationCoordinates;
        }

    }


    @Override
    public void render(Batch batch) {
        graphicsAbstraction.render(batch, getGraphics(), getRectangle(), rotation);
    }

    @Override
    public void dispose(){

    }

    public GridPoint2 getCurrentCoordinates() { return currentCoordinates;}

    public int getDamage() {
        return DAMAGE;
    }

    public boolean isReadyForNextMove() {
        return isEqual(movementProgress, 1f);
    }

    public final float getRotation()
    {
        return rotation;
    }

    public Tank getShooter(){
        return shooter;
    }
}