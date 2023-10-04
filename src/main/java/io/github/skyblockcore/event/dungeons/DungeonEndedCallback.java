package io.github.skyblockcore.event.dungeons;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface DungeonEndedCallback {
    Event<DungeonEndedCallback> EVENT = EventFactory.createArrayBacked(DungeonEndedCallback.class,
            (listeners) -> (score) -> {
                for (DungeonEndedCallback listener : listeners) {
                    ActionResult result = listener.interact(score);

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(int score);
}
