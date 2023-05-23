package io.github.skyblockcore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.skyblockcore.event.ConfigManager;
import io.github.skyblockcore.event.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static io.github.skyblockcore.SkyblockCore.LOGGER;
import static io.github.skyblockcore.SkyblockCore.TITLE;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SkyblockCoreCommand {

    public static boolean NBTCOPYING = false;


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> skyblockcore = literal("skyblockcore");
        skyblockcore.then(literal("reload").executes(ctx -> reloadConfig()));
        skyblockcore.then(literal("nbt").executes(ctx -> nbt()));
        skyblockcore.then(literal("dev").executes(ctx -> dev()));
        skyblockcore.then(literal("location").executes(ctx -> location()));
        dispatcher.register(skyblockcore);
    }

    private static int reloadConfig() {
        ConfigManager.reloadConfig(); // Reload the config
        LOGGER.info(TITLE + " Reloaded Config!");
        if (MinecraftClient.getInstance().player == null) return 0;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Config file Reloaded in: " + System.currentTimeMillis() + " ms").formatted(Formatting.WHITE), false);


        return Command.SINGLE_SUCCESS;
    }

    private static int nbt() {
        // Practically done. Needs help in the main class for copying.
        if (MinecraftClient.getInstance().player == null) return 0;
        NBTCOPYING = !NBTCOPYING;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " NBT copying has been " + (NBTCOPYING ? "enabled! (Tip, use Right Control to copy items NBT data to clipboard.)" : "disabled!")).formatted(Formatting.WHITE), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int dev() {
        ModConfig config = ConfigManager.getConfig();
        if (config != null) {
            boolean currentDevSetting = config.isDev();
            config.setDev(!currentDevSetting); // Toggle the dev mode setting
            ConfigManager.saveConfig(); // Save the updated config
            LOGGER.info(TITLE + " Successfully " + (currentDevSetting ? "disabled" : "enabled") + " Developer mode!");
            if (MinecraftClient.getInstance().player == null) return 0;
            MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Dev mode " + (currentDevSetting ? "Disabled" : "Enabled")).formatted(Formatting.WHITE), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int location() {
        ModConfig config = ConfigManager.getConfig();
        if (config != null) {
            boolean currentLocationSetting = config.isLocation();
            config.setLocation(!currentLocationSetting); // Toggle the location setting
            ConfigManager.saveConfig(); // Save the updated config
            LOGGER.info(TITLE + " Successfully " + (currentLocationSetting ? "disabled" : "enabled") + " location logging!");
            if (MinecraftClient.getInstance().player == null) return 0;
            MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Location data " + (currentLocationSetting ? "Disabled" : "Enabled")).formatted(Formatting.WHITE), false);
        }


        return Command.SINGLE_SUCCESS;
    }
}
