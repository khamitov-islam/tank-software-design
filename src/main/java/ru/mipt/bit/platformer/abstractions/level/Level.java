package ru.mipt.bit.platformer.abstractions.level;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.abstractions.controllers.AITankController;
import ru.mipt.bit.platformer.abstractions.controllers.CollisionController;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.Tank;

import java.util.List;
import java.util.Set;

public interface Level {


    void generate(List<BaseModel> models, TiledMapTileLayer groundLayer, GraphicsAbstraction graphicsAbstraction);
    int getLevelWidth();
    int getLevelHeight();
    List<AITankController> getAIControllers();
    Tank getPlayerTank();

}
