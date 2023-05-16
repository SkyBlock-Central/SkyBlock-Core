package io.github.skyblockcore.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface JoinSkyblockCallback {

    Event<JoinSkyblockCallback> EVENT = EventFactory.createArrayBacked(JoinSkyblockCallback.class,
            (listeners) -> () -> {
                for (JoinSkyblockCallback listener : listeners) {
                    ActionResult result = listener.interact();

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact();

}
