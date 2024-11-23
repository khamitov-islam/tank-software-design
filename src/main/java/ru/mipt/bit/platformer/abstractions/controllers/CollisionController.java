package ru.mipt.bit.platformer.abstractions.controllers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.HashSet;
import java.util.Set;

public class CollisionController {
    private final TileMovement tileMovement;
    private final Set<GridPoint2> occupiedPositions;

    public CollisionController(TileMovement tileMovement) {
        this.tileMovement = tileMovement;
        this.occupiedPositions = new HashSet<>();
    }

    public boolean canMove(Tank tank, Direction direction) {
        GridPoint2 currentPos = tank.getPosition();
        GridPoint2 nextPos = direction.move(currentPos);


        // Проверка границ
        if (!isWithinBounds(nextPos)) {
            return false;
        }

        // Проверка занятости клетки
        if (isPositionOccupied(nextPos)) {
            return false;
        }

        return true;
    }

    public boolean isWithinBounds(GridPoint2 position) {
        return position.x >= 0 && position.x < tileMovement.getWidth()
                && position.y >= 0 && position.y < tileMovement.getHeight();
    }

    public void addOccupiedPosition(GridPoint2 newPos) {
        occupiedPositions.add(newPos);
    }

    public boolean isPositionOccupied(GridPoint2 position) {
        return occupiedPositions.contains(position);
    }

    public void clear(){
        occupiedPositions.clear();
    }

}
