package ru.mipt.bit.platformer.abstractions.command;

import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.abstractions.models.Tank;

public class MoveTankCommand implements Command{
    private final Tank tank;
    private final Direction direction;

    public MoveTankCommand(Tank tank, Direction direction){
        this.tank = tank;
        this.direction = direction;
    }

    public void execute(){
        tank.move(direction);
    }
}
