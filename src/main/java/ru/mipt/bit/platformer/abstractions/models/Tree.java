package ru.mipt.bit.platformer.abstractions.models;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import ru.mipt.bit.platformer.abstractions.Collidable;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.Renderable;


public class Tree extends BaseModel implements Renderable, Collidable {
    public Tree(String texturePath, GridPoint2 initialPosition, TiledMapTileLayer layer, GraphicsAbstraction graphicsAbstraction) {
        super(texturePath, initialPosition, graphicsAbstraction);
        moveRectangleAtTileCenter(layer, getRectangle(), getPosition());
    }
    public void render(Batch batch) {

        graphicsAbstraction.render(batch, getGraphics(), getRectangle(), 0f);
    }
    public void dispose() {
        graphicsAbstraction.dispose();
        getTexture().dispose();
    }
    @Override
    public boolean isCollides(GridPoint2 point) {
        return getPosition().equals(point);
    }
}