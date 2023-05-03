package io.github.skyblockcore;

import io.github.skyblockcore.event.JoinSkyblockCallback;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockCore implements ClientModInitializer {
    public static final String ModID = "skyblockcore";

    private static boolean ON_SKYBLOCK = false;
    public static boolean isOnSkyblock() { return ON_SKYBLOCK; }

    public static final Logger LOGGER = LoggerFactory.getLogger(ModID);

    @Override
    public void onInitializeClient() {
        // example of event, TODO; move to better class
        JoinSkyblockCallback.EVENT.register(() -> {
            ON_SKYBLOCK = true;
            System.out.println("Skyblock joined");
            return ActionResult.PASS;
        });
    }
}
