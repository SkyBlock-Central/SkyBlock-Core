package io.github.skyblockcore.event.dungeons;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface WitherKeyObtainedCallback {
    Event<WitherKeyObtainedCallback> EVENT = EventFactory.createArrayBacked(WitherKeyObtainedCallback.class,
            (listeners) -> (obtainerUsername) -> {
                for (WitherKeyObtainedCallback listener : listeners) {
                    ActionResult result = listener.interact(obtainerUsername);

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(String obtainerUsername);
}
