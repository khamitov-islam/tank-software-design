package ru.mipt.bit.platformer.abstractions;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;
import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank {
    private final Texture tankTexture;
    private final TextureRegion tankGraphics;
    private final Rectangle tankRectangle;
    private final float movementSpeed;

    private GridPoint2 currentCoordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;

    public Tank(String texturePath, GridPoint2 initialCoordinates, float movementSpeed) {
        this.tankTexture = new Texture(texturePath);
        this.tankGraphics = new TextureRegion(tankTexture);
        this.tankRectangle = createBoundingRectangle(tankGraphics);
        this.currentCoordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
        this.rotation = 0f;
    }

    public void handleInput() {
        if (isEqual(movementProgress, 1f)) {
            if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
                destinationCoordinates.x--;
                rotation = -180f;
                movementProgress = 0f;
            }

            if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
                destinationCoordinates.x++;
                rotation = 0f;
                movementProgress = 0f;
            }

            if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
                destinationCoordinates.y++;
                rotation = 90f;
                movementProgress = 0f;
            }

            if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
                destinationCoordinates.y--;
                rotation = -90f;
                movementProgress = 0f;
            }

        }
    }

    public void updatePosition(TileMovement tileMovement, float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(tankRectangle, currentCoordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            currentCoordinates.set(destinationCoordinates);
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, tankGraphics, tankRectangle, rotation);
    }

    public void dispose() {
        tankTexture.dispose();
    }

    public GridPoint2 getDestination() {
        return new GridPoint2(destinationCoordinates);
    }
}