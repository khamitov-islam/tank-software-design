package ru.mipt.bit.platformer.abstractions.handlers;
import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.abstractions.models.Bullet;
import ru.mipt.bit.platformer.abstractions.models.Direction;
import static com.badlogic.gdx.Input.Keys.*;

public class TankInputHandler implements InputHandler {

    @Override
    public Direction handleMovingInput() {
        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            return Direction.UP;
        } else if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
            return Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            return Direction.DOWN;
        } else if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
            return Direction.RIGHT;
        }
        return null;
    }

    //to do : shooting input
    @Override
    public boolean handleShootingInput(){
        return Gdx.input.isKeyPressed(SPACE);
    }

}