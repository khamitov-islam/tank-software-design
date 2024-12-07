package ru.mipt.bit.platformer.abstractions.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.Renderable;
import ru.mipt.bit.platformer.abstractions.models.HealthBarModel;
import ru.mipt.bit.platformer.util.GdxGameUtils;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;


public class HealthBarDecorator implements Renderable { //
    private static final int HEALTH_BAR_WIDTH = 90;
    private static final int HEALTH_BAR_HEIGHT = 20;
    private static final int HEALTH_BAR_Y_OFFSET = 90;
    private final Liveable model;
    private Texture currentTexture;


    public HealthBarDecorator(Liveable model) {
        this.model = model;
    }

    @Override
    public void render(Batch batch) {
        renderHealthBar(batch);
    }

    @Override
    public void dispose() {
        if (currentTexture != null) {
            currentTexture.dispose();
        }

    }

    private void renderHealthBar(Batch batch) {
        if (!HealthBarModel.getVisible()) {
            return;
        }

        System.out.println(model.getHealth());
        float relativeHealth = (float) model.getHealth() / 100f;
        System.out.println(relativeHealth);
        TextureRegion healthBarTexture = createHealthBarTexture(relativeHealth);
        Rectangle healthBarRectangle = createHealthBarRectangle();
        drawTextureRegionUnscaled(batch, healthBarTexture, healthBarRectangle, 0f);
    }


    private TextureRegion createHealthBarTexture(float relativeHealth) {
        if (currentTexture != null) {
            currentTexture.dispose();
        }

        Pixmap pixmap = new Pixmap(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT, Pixmap.Format.RGBA8888);
        
        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(0, 0, (int) (HEALTH_BAR_WIDTH * relativeHealth), HEALTH_BAR_HEIGHT);
        pixmap.setColor(Color.GRAY);
        pixmap.fillRectangle((int) (HEALTH_BAR_WIDTH * relativeHealth), 0, HEALTH_BAR_WIDTH - (int) (HEALTH_BAR_WIDTH * relativeHealth), HEALTH_BAR_HEIGHT);
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        currentTexture = texture;

        return new TextureRegion(texture);
    }
    private Rectangle createHealthBarRectangle() {
        Rectangle rectangle = new Rectangle(model.getRectangle());
        rectangle.y += HEALTH_BAR_Y_OFFSET;
        return rectangle;
    }
}
