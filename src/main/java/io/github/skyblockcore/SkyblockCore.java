package io.github.skyblockcore;

import io.github.skyblockcore.event.JoinSkyblockCallback;
import io.github.skyblockcore.event.LeaveSkyblockCallback;
import io.github.skyblockcore.event.LocationChangedCallback;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SkyblockCore implements ClientModInitializer {
    public static final String ModID = "skyblockcore";

    private static boolean ON_SKYBLOCK = false;
    public static boolean isOnSkyblock() { return ON_SKYBLOCK; }
    public static String getLocation() { return LOCATION; }
    private static String LOCATION;

    public static final Logger LOGGER = LoggerFactory.getLogger(ModID);

    @Override
    public void onInitializeClient() {
        // example of events, TODO; move to better class
        JoinSkyblockCallback.EVENT.register(() -> {
            ON_SKYBLOCK = true;
            return ActionResult.PASS;
        });
        LeaveSkyblockCallback.EVENT.register(() -> {
            ON_SKYBLOCK = false;
            LOCATION = null;
            return ActionResult.PASS;
        });
        LocationChangedCallback.EVENT.register(((oldLocation, newLocation) -> {
            LOCATION = newLocation;
            return ActionResult.PASS;
        }));
    }
}
