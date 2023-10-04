package io.github.skyblockcore.event.dungeons;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface EnteredBossfightCallback {
    Event<EnteredBossfightCallback> EVENT = EventFactory.createArrayBacked(EnteredBossfightCallback.class,
            (listeners) -> () -> {
                for (EnteredBossfightCallback listener : listeners) {
                    ActionResult result = listener.interact();

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact();
}
