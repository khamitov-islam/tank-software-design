package ru.mipt.bit.platformer.abstractions.controllers;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.abstractions.models.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;

public class CollisionController {
    private  TileMovement tileMovement;
    public Set<GridPoint2> occupiedPositions;


    public CollisionController(TileMovement tileMovement) { 
        this.tileMovement = tileMovement;
        this.occupiedPositions = new HashSet<>();
    }

    public List<Direction> getAvailableDirections(Tank tank) {
        List<Direction> availableDirections = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            GridPoint2 nextPosition = direction.move(tank.getCurrentCoordinates());
            if (isValidMove(nextPosition)) {
                availableDirections.add(direction);
            }
        }
        return availableDirections;
    }

    public Direction getNextDirection(Tank tank) {
        List<Direction> availableDirections = getAvailableDirections(tank);
        if (!availableDirections.isEmpty()) {
            return availableDirections.get(random.nextInt(availableDirections.size()));
        }
        return null;
    }

    public boolean isValidMove(GridPoint2 nextPosition) {
        return (isWithinBounds(nextPosition) && !isPositionOccupied(nextPosition));
    }

    public boolean isWithinBounds(GridPoint2 position) {
        boolean result = position.x >= 0 && position.x < tileMovement.getWidth()
                && position.y >= 0 && position.y < tileMovement.getHeight();
        //System.out.println("isWithinBounds check for " + position + ": " + result);
        return result;
    }

    public void addOccupiedPosition(GridPoint2 newPos) {
        occupiedPositions.add(newPos);
    }

    public boolean isPositionOccupied(GridPoint2 position) {
        return occupiedPositions.contains(position);
    }

    public void clearOccupiedPositions(){ occupiedPositions.clear(); }

    public void update(List<BaseModel> models) {
        clearOccupiedPositions();

        for (BaseModel model : models) {
            if (model instanceof Tree){
                addOccupiedPosition(model.getPosition());
            }
            if (model instanceof Tank) {
                Tank tank = (Tank) model;
                addOccupiedPosition(tank.getCurrentCoordinates());
            }
        }
    }
}

