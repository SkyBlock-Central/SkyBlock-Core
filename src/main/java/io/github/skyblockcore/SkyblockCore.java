package io.github.skyblockcore;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockCore implements ClientModInitializer {
    public static final String ModID = "skyblockcore";

    public static final Logger LOGGER = LoggerFactory.getLogger(ModID);

    @Override
    public void onInitializeClient() {
        System.out.println("Hello World");
    }
}
