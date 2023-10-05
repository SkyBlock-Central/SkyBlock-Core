package io.github.skyblockcore.event.dungeons;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface LeaveDungeonCallback {
    Event<LeaveDungeonCallback> EVENT = EventFactory.createArrayBacked(LeaveDungeonCallback.class,
            (listeners) -> () -> {
                for (LeaveDungeonCallback listener : listeners) {
                    ActionResult result = listener.interact();

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact();
}
