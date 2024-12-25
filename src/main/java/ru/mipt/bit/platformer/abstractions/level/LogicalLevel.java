package ru.mipt.bit.platformer.abstractions.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.command.ToggleHealthDisplayCommand;
import ru.mipt.bit.platformer.abstractions.controllers.AITankController;
import ru.mipt.bit.platformer.abstractions.controllers.CollisionController;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Bullet;
import ru.mipt.bit.platformer.abstractions.models.Direction;
import ru.mipt.bit.platformer.abstractions.models.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class LogicalLevel implements Observable {
    private List<Observer> observers = new ArrayList<>();
    private final List<BaseModel> models;
    private final TileMovement tileMovement;
    private final GraphicsAbstraction graphicsAbstraction;
    private final Tank playerTank;
    private final List<AITankController> aiControllers;
    public CollisionController collisionController;

    public LogicalLevel(List<BaseModel> models,
                        TileMovement tileMovement,
                        GraphicsAbstraction graphicsAbstraction,
                        CollisionController collisionController,
                        Tank playerTank,
                        List<AITankController> aiControllers
    ) {
        this.models = models;
        this.tileMovement = tileMovement;
        this.graphicsAbstraction = graphicsAbstraction;
        this.collisionController = collisionController;
        this.playerTank = playerTank;
        this.aiControllers = aiControllers;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObjectAdded(BaseModel model) {
        for (Observer observer : observers) {
            observer.onObjectAdded(model);
        }
    }

    @Override
    public void notifyObjectRemoved(BaseModel model) {
        for (Observer observer : observers) {
            observer.onObjectRemoved(model);
        }
    }

    public void updateModels(float deltaTime) {

        for (BaseModel model : models) {
            if (model instanceof Tank) {
                ((Tank) model).updateCooldown(deltaTime);
            }
        }

        // Обработка стрельбы игрока
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bullet = playerTank.shoot();
            if (bullet != null) {
                notifyObjectAdded(bullet);
                models.add(bullet);
            }
        }


        handleBulletCollisions();

        collisionController.updateOccupiedPositions(models);


        playerTank.handleInput();
         
        if (collisionController.isValidMove(playerTank.getDestination())) {
            playerTank.updatePosition(tileMovement, deltaTime);
        } else {
            playerTank.cancelMovement();
        }

        for (AITankController aiController : aiControllers) {
            if (random.nextFloat() < 0.01) {
                Bullet bullet = aiController.getAiTank().shoot();
                notifyObjectAdded(bullet);
                models.add(bullet);
            }
            aiController.update(deltaTime, collisionController);
        }

        for (BaseModel model : models) {
            if (model instanceof Bullet) {
                Bullet bullet = (Bullet) model;
                bullet.move(Direction.getDirection(bullet.getRotation()));
                bullet.updatePosition(tileMovement, deltaTime);
            }
        }

    }

    public void handleBulletCollisions() {
        Iterator<BaseModel> bulletIterator = models.iterator();
        List<BaseModel> modelsToRemove = new ArrayList<>();

        while (bulletIterator.hasNext()) {
            BaseModel model = bulletIterator.next();
            if (!(model instanceof Bullet)) {
                continue;
            }

            Bullet bullet = (Bullet) model;

            if (!collisionController.isWithinBounds(bullet.getCurrentCoordinates())) {
                modelsToRemove.add(bullet);
                continue;
            }

            for (BaseModel target : models) {
                if (target == bullet) {
                    continue;
                }

                if (collisionController.isCollision(bullet, target)) {
                    collisionController.handleCollision(bullet, target, modelsToRemove);
                    break; // Пуля уже столкнулась, прекращаем проверку других объектов
                }
            }
        }

        for (BaseModel model : modelsToRemove) {
            models.remove(model);
            notifyObjectRemoved(model);
        }

    }
}