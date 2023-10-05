package io.github.skyblockcore.event.dungeons;

import io.github.skyblockcore.Dungeons;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface EnteredBossfightCallback {
    Event<EnteredBossfightCallback> EVENT = EventFactory.createArrayBacked(EnteredBossfightCallback.class,
            (listeners) -> (boss) -> {
                for (EnteredBossfightCallback listener : listeners) {
                    ActionResult result = listener.interact(boss);

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(Dungeons.DUNGEON_BOSSES boss);
}
