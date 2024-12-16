package ru.mipt.bit.platformer.abstractions;

import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.util.TileMovement;

public interface Movable {
    public void move(Direction direction);

    public void updatePosition(TileMovement tileMovement, float deltaTime);

//    public void cancelMovement();

}
