package ru.mipt.bit.platformer.abstractions.level;

import ru.mipt.bit.platformer.abstractions.level.Observer;
import ru.mipt.bit.platformer.abstractions.models.BaseModel;


public interface Observable {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObjectAdded(BaseModel model);
    public void notifyObjectRemoved(BaseModel model);
}