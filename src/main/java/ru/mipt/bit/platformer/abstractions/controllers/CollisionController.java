package ru.mipt.bit.platformer.abstractions.controllers;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.models.*;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.*;

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

    public void updateOccupiedPositions(List<BaseModel> models) {
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

    public boolean isCollision(Bullet bullet, BaseModel target) {
        if (target == null) {
            return false;
        }
        // Если цель - это танк, который выстрелил эту пулю, игнорируем столкновение
        if (target instanceof Tank && target == bullet.getShooter()) {
            return false;
        }
        GridPoint2 bulletPos = bullet.getCurrentCoordinates();

        if (target instanceof Tank) {
            return bulletPos.equals(((Tank) target).getCurrentCoordinates());
        } else if (target instanceof Bullet) {
            return bulletPos.equals(((Bullet) target).getCurrentCoordinates());
        } else {
            return bulletPos.equals(target.getPosition());
        }
    }

    public void handleCollision(Bullet bullet, BaseModel target, List<BaseModel> modelsToRemove) {
        if (target instanceof Tank) {
            handleTankBulletCollision((Tank) target, bullet, modelsToRemove);
        } else {
            // Для столкновения с другими объектами (пуля или препятствие)
            modelsToRemove.add(target);
            modelsToRemove.add(bullet);
        }
    }

    public void handleTankBulletCollision(Tank tank, Bullet bullet, List<BaseModel> modelsToRemove) {
        tank.setHealth(tank.getHealth() - bullet.getDamage());
        modelsToRemove.add(bullet);

        if (tank.getHealth() <= 0) {
            modelsToRemove.add(tank);
            //todo: удалить иконку жизней погибшего танка
        }
    }


}

