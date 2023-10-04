package io.github.skyblockcore.event.dungeons;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface WitherDoorOpenedCallback {
    Event<WitherDoorOpenedCallback> EVENT = EventFactory.createArrayBacked(WitherDoorOpenedCallback.class,
            (listeners) -> (openerUsername) -> {
                for (WitherDoorOpenedCallback listener : listeners) {
                    ActionResult result = listener.interact(openerUsername);

                    if (result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(String openerUsername);
}
