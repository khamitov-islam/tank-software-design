package ru.mipt.bit.platformer.abstractions;

import ru.mipt.bit.platformer.util.TileMovement;

public interface Movable {
    public void handleInput();

    public void updatePosition(TileMovement tileMovement, float deltaTime);

    public void cancelMovement();

}
