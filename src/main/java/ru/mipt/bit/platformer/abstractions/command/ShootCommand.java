package ru.mipt.bit.platformer.abstractions.command;

import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Tank;

import java.util.List;

public class ShootCommand implements Command {
    private final Tank tank;

    public ShootCommand(Tank tank) {
        this.tank = tank;
    }

    public void execute() {
        tank.shoot();
    }
}