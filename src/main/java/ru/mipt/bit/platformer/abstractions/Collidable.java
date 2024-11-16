package ru.mipt.bit.platformer.abstractions;
import com.badlogic.gdx.math.GridPoint2;

public interface Collidable {
    boolean isCollides(GridPoint2 point);
}
