//package ru.mipt.bit.platformer.abstractions.controllers;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.math.GridPoint2;
//import com.badlogic.gdx.utils.compression.lzma.Base;
//import ru.mipt.bit.platformer.abstractions.Liveable;
//import ru.mipt.bit.platformer.abstractions.Renderable;
//import ru.mipt.bit.platformer.abstractions.command.MoveTankCommand;
//import ru.mipt.bit.platformer.abstractions.command.ToggleHealthDisplayCommand;
//import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
//import ru.mipt.bit.platformer.abstractions.level.Level;
//import ru.mipt.bit.platformer.abstractions.models.*;
//import ru.mipt.bit.platformer.abstractions.Movable;
//import ru.mipt.bit.platformer.util.TileMovement;
//import java.util.List;
//
//import static com.badlogic.gdx.math.MathUtils.random;
//
//public class ModelController {
//    // private final ToggleHealthDisplayCommand toggleHealthDisplayCommand;
//    private final List<BaseModel> models;
//    private final TileMovement tileMovement;
//    private final GraphicsAbstraction graphicsAbstraction;
//    private final Tank playerTank;
//    private final List<AITankController> aiControllers;
//    public CollisionController collisionController;
//
//    public ModelController(List<BaseModel> models, TileMovement tileMovement,
//                           GraphicsAbstraction graphicsAbstraction,
//                           CollisionController collisionController,
//                           Tank playerTank , List<AITankController> aiControllers,
//                           ToggleHealthDisplayCommand toggleHealthDisplayCommand
//    ) {
//        this.models = models;
//        this.tileMovement = tileMovement;
//        this.graphicsAbstraction = graphicsAbstraction;
//        this.collisionController = collisionController;
//        this.playerTank = playerTank;
//        this.aiControllers = aiControllers;
//        // this.toggleHealthDisplayCommand = toggleHealthDisplayCommand;
//    }
//
//
//    public void updateModels(float deltaTime) {
//        // Обновляем кулдаун стрельбы для всех танков
//        for (BaseModel model : models) {
//            if (model instanceof Tank) {
//                ((Tank) model).updateCooldown(deltaTime);
//            }
//        }
//
//        // Обработка стрельбы игрока
//        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
//            Bullet bullet = playerTank.shoot();
//            if (bullet != null) {
//                // оповестить наблюдателя о появлении новой модели для отрисовки
//                models.add(bullet);
//            }
//        }
//
//
//        // Обработка столкновений пуль
//        collisionController.handleBulletCollisions(models);
//
//
//        // Существующий код обновления
//        collisionController.updateOccupiedPositions(models);
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
//            toggleHealthDisplayCommand.execute();
//        }
//
//        playerTank.handleInput();
//        if (collisionController.isValidMove(playerTank.getDestination())) {
//            playerTank.updatePosition(tileMovement, deltaTime);
//        } else {
//            playerTank.cancelMovement();
//        }
//
//        for (AITankController aiController : aiControllers) {
//            if (random.nextFloat() < 0.25) { // 25% шанс выстрела каждый фрейм
//                Bullet bullet = aiController.getAiTank().shoot();
//                models.add(bullet);
//                // оповестить Observer о появлении пули для отрисовки в логическом уровне
//                aiController.update(deltaTime, collisionController);
//            }
//        }
//    }
//
//
//
//}