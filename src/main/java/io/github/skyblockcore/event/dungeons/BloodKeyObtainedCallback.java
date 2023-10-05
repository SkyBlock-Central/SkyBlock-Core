package io.github.skyblockcore.event.dungeons;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface BloodKeyObtainedCallback {
    Event<BloodKeyObtainedCallback> EVENT = EventFactory.createArrayBacked(BloodKeyObtainedCallback.class,
            (listeners) -> (obtainerUsername) -> {
                for (BloodKeyObtainedCallback listener : listeners) {
                    ActionResult result = listener.interact(obtainerUsername);

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(String obtainerUsername);
}
