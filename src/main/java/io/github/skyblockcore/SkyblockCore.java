package io.github.skyblockcore;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.skyblockcore.gui.TestGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockCore implements ClientModInitializer {
    public static final String ModID = "skyblockcore";

    public static final Logger LOGGER = LoggerFactory.getLogger(ModID);
    Screen setTo = null;

    @Override
    public void onInitializeClient() {
        System.out.println("Hello World");
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register((LiteralArgumentBuilder) LiteralArgumentBuilder.literal("test")
                .executes(context -> {
                    setTo = new TestGui();
                    return 1;
                })));
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (setTo == null) return;
            client.setScreen(setTo);
            setTo = null;
        });
    }

}
