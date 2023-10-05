package io.github.skyblockcore.event.dungeons;

import io.github.skyblockcore.Dungeons;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface DungeonStartedCallback {
    Event<DungeonStartedCallback> EVENT = EventFactory.createArrayBacked(DungeonStartedCallback.class,
            (listeners) -> (dungeonClass) -> {
                for (DungeonStartedCallback listener : listeners) {
                    ActionResult result = listener.interact(dungeonClass);

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(Dungeons.DUNGEON_CLASSES dungeonClass);
}