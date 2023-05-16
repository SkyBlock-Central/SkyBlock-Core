package io.github.skyblockcore.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface LocationChangedCallback {

    Event<LocationChangedCallback> EVENT = EventFactory.createArrayBacked(LocationChangedCallback.class,
            (listeners) -> (oldLocation, newLocation) -> {
                for (LocationChangedCallback listener : listeners) {
                    ActionResult result = listener.interact(oldLocation, newLocation);

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(String oldLocation, String newLocation);

}
