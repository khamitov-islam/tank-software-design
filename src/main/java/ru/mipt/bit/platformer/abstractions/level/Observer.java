package ru.mipt.bit.platformer.abstractions.level;

import ru.mipt.bit.platformer.abstractions.models.BaseModel;

public interface Observer {
    void onObjectAdded(BaseModel model);
    void onObjectRemoved(BaseModel model);
}
