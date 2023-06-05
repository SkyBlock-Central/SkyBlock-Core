package io.github.skyblockcore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.skyblockcore.event.ConfigManager;
import io.github.skyblockcore.event.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static io.github.skyblockcore.SkyblockCore.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;


public class SkyblockCoreCommand {
    public static boolean NBTCOPYING = false;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> skyblockcore = literal("skyblockcore");
        skyblockcore.then(literal("reload").executes(ctx -> reloadConfig()));
        skyblockcore.then(literal("nbt").executes(ctx -> nbt()));
        skyblockcore.then(literal("dev").executes(ctx -> dev()));
        skyblockcore.then(literal("test").executes(ctx -> test()));
        skyblockcore.then(literal("LogSpam").executes(ctx -> player()));
        skyblockcore.then(literal("location").executes(ctx -> location()));
        dispatcher.register(skyblockcore);
    }

    private static int test() {
        // example command
        if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isDev()) {
            sendDebugLog("woah!! [dev]");
        }
        if (MinecraftClient.getInstance().player == null) return 0;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Test command, cool that you found this, easter egg ig! well anyways, hi from Axle!").formatted(Formatting.AQUA), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadConfig() {
        ConfigManager.reloadConfig(); // Reload the config
        sendDebugLog("Reloaded Config!");
        if (MinecraftClient.getInstance().player == null) return 0;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Config file Reloaded in: " + System.currentTimeMillis() + " ms").formatted(Formatting.WHITE), false);


        return Command.SINGLE_SUCCESS;
    }

    private static int nbt() {
        // Practically done. Needs help in the main class for copying.
        // TODO Implement NBT Handling Logic here
        // This will be similar to "/sba dev" or "/skytils dev nbt".
        if (MinecraftClient.getInstance().player == null) return 0;
        NBTCOPYING = !NBTCOPYING;
        Text keybindingEnabled = Text.literal("enabled! (Tip: use ").formatted(Formatting.WHITE).append(Text.keybind("skyblockcore.dev.nbtcopy").formatted(Formatting.YELLOW)).append(Text.literal(" to copy an items NBT data to clipboard.)").formatted(Formatting.WHITE));
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " NBT copying has been ").formatted(Formatting.WHITE).append((NBTCOPYING ? keybindingEnabled : Text.literal("disabled!").formatted(Formatting.WHITE))), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int dev() {
        ModConfig config = ConfigManager.getConfig();
        if (config != null) {
            boolean currentDevSetting = config.isDev();
            config.setDev(!currentDevSetting); // Toggle the dev mode setting
            ConfigManager.saveConfig(); // Save the updated config
            sendDebugLog("Successfully " + (currentDevSetting ? "disabled" : "enabled") + " Developer mode!");
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
            sendDebugLog("Successfully " + (currentLocationSetting ? "disabled" : "enabled") + " location logging!");
            if (MinecraftClient.getInstance().player == null) return 0;
            MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Location data " + (currentLocationSetting ? "Disabled" : "Enabled")).formatted(Formatting.WHITE), false);
        }


        return Command.SINGLE_SUCCESS;
    }


    private static int player() {
        // example command
        ModConfig config = ConfigManager.getConfig();
        if (config != null) {
            boolean currentPlayerSetting = config.isUnknownPlayer();
            config.setUnknownPlayer(!currentPlayerSetting); // Toggle the location setting
            ConfigManager.saveConfig(); // Save the updated config
            if (MinecraftClient.getInstance().player == null) return 0;
            if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isDev()) {
                sendDebugLog("Warning! You have " + (currentPlayerSetting ? "disabled" : "enabled") + " \"Ignoring player info update\"");
            }
            MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Warning! You have " + (currentPlayerSetting ? "disabled" : "enabled") + " \"Ignoring player info update\"").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), false);
        }
        return Command.SINGLE_SUCCESS;
    }

}
