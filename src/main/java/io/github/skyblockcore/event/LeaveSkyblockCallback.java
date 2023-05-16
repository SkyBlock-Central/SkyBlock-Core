package io.github.skyblockcore.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface LeaveSkyblockCallback {

    Event<LeaveSkyblockCallback> EVENT = EventFactory.createArrayBacked(LeaveSkyblockCallback.class,
            (listeners) -> () -> {
                for (LeaveSkyblockCallback listener : listeners) {
                    ActionResult result = listener.interact();

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact();

}
