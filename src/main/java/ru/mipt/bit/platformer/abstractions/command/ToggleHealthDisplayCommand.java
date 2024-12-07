package ru.mipt.bit.platformer.abstractions.command;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;
import ru.mipt.bit.platformer.abstractions.models.HealthBarModel;

import java.util.*;

public class ToggleHealthDisplayCommand implements Command{
    private boolean isHealthDisplayEnabled = false;
    private Collection<Liveable> modelsWithHealth;
    private Batch batch;
    private Map<Liveable, HealthBarDecorator> healthBars;

    public ToggleHealthDisplayCommand(List<BaseModel> models , Batch batch)
    {
        modelsWithHealth = new ArrayList<>();
        healthBars = new HashMap<>();
        for (BaseModel model: models){
            if (model instanceof Liveable){
                Liveable modelWithHealth = (Liveable) model;
                modelsWithHealth.add(modelWithHealth);
                healthBars.put(modelWithHealth, new HealthBarDecorator(modelWithHealth));

            }
        }
        this.batch = batch;
    }


    @Override
    public void execute() {
        isHealthDisplayEnabled = !isHealthDisplayEnabled;
        HealthBarModel.switchVisible();
    }

    public void showHealthDisplay() { // showHealthDisplay
        for (Liveable model : modelsWithHealth) {
            HealthBarDecorator decorator = healthBars.get(model);
            decorator.render(batch);
        }
    }

    public boolean isEnabled() {
        return isHealthDisplayEnabled;
    }
}
