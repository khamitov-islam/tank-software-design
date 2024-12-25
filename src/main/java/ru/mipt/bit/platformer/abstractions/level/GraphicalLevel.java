package ru.mipt.bit.platformer.abstractions.level;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.abstractions.Liveable;
import ru.mipt.bit.platformer.abstractions.Renderable;
import ru.mipt.bit.platformer.abstractions.command.ToggleHealthDisplayCommand;
import ru.mipt.bit.platformer.abstractions.graphics.GraphicsAbstraction;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;

import java.util.ArrayList;
import java.util.List;


public class GraphicalLevel implements Observer{

    private GraphicsAbstraction graphicsAbstraction;
    private List<BaseModel> models;
    private ToggleHealthDisplayCommand toggleHealthDisplayCommand;

    public GraphicalLevel(GraphicsAbstraction graphicsAbstraction, List<BaseModel> models, ToggleHealthDisplayCommand toggleHealthDisplayCommand){
        this.graphicsAbstraction = graphicsAbstraction;
        this.models = models;
        this.toggleHealthDisplayCommand = toggleHealthDisplayCommand;
    }

    public void renderModels(Batch batch) {
        for (BaseModel model : models) {
            if (model instanceof Renderable) {
                ((Renderable) model).render(batch);

            }
        }

        if (toggleHealthDisplayCommand.isEnabled()){
            toggleHealthDisplayCommand.showHealthDisplay(); }

    }

    public void disposeModels() {
        for (BaseModel model : models) {
            model.dispose();
        }
    }

    @Override
    public void onObjectAdded(BaseModel model) {
            models.add(model);
    }

    @Override
    public void onObjectRemoved(BaseModel model) {
        if (model instanceof Liveable){
            Liveable modelWithHealth = (Liveable) model;
            toggleHealthDisplayCommand.removeModel(modelWithHealth);
        }
        models.remove(model);
    }

}
