package io.github.skyblockcore.event.dungeons;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface BloodDoorOpenedCallback {
    Event<BloodDoorOpenedCallback> EVENT = EventFactory.createArrayBacked(BloodDoorOpenedCallback.class,
            (listeners) -> () -> {
                for (BloodDoorOpenedCallback listener : listeners) {
                    ActionResult result = listener.interact();

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact();
}