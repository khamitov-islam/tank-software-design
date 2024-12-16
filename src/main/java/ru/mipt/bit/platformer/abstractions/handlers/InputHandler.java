package ru.mipt.bit.platformer.abstractions.handlers;
import ru.mipt.bit.platformer.abstractions.models.Bullet;
import ru.mipt.bit.platformer.abstractions.models.Direction;

public interface InputHandler {
    Direction handleMovingInput();
    boolean handleShootingInput();
}