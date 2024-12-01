package ru.mipt.bit.platformer.abstractions.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.abstractions.controllers.GraphicsAbstraction;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public abstract class BaseModel {

    private Texture texture;
    private TextureRegion graphics;
    private Rectangle rectangle;
    private GridPoint2 position;

    protected final GraphicsAbstraction graphicsAbstraction;

    BaseModel(String texturePath, GridPoint2 initialPosition, GraphicsAbstraction graphicsAbstraction) {
        this.texture = new Texture(texturePath);
        this.graphicsAbstraction = graphicsAbstraction;
        this.graphics = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(graphics);
        this.position = new GridPoint2(initialPosition);
    }

    public Texture getTexture() {
        return texture;
    }

    public TextureRegion getGraphics() {
        return graphics;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public GridPoint2 getPosition() {
        return position;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setGraphics(TextureRegion graphics) {
        this.graphics = graphics;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setPosition(GridPoint2 position) {
        this.position = position;
    }

    public void render(Batch batch) {
        graphicsAbstraction.render(batch, getGraphics(), getRectangle(), 0f);
    }

    public void dispose() {
        graphicsAbstraction.dispose();
        getTexture().dispose();
    }

    public boolean isCollides(GridPoint2 point) {
        return getPosition().equals(point);
    }
}