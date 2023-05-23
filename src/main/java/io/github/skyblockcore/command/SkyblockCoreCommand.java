package io.github.skyblockcore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static io.github.skyblockcore.SkyblockCore.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class SkyblockCoreCommand {



    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> skyblockcore = literal("skyblockcore");
        skyblockcore.then(literal("reload").executes(ctx -> reloadConfig()));
        skyblockcore.then(literal("nbt").executes(ctx -> nbt()));
        skyblockcore.then(literal("dev").executes(ctx -> dev()));
        skyblockcore.then(literal("location").executes(ctx -> location()));
        dispatcher.register(skyblockcore);
    }

    private static int reloadConfig() {
        loadConfig();
        // TODO Send user feedback in Minecraft Chat
        LOGGER.info(TITLE + " Reloaded Config!");
        if (MinecraftClient.getInstance().player == null) return 0;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Config file Reloaded in: timeMS" /* TODO add time ms */).formatted(Formatting.WHITE), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int nbt() {
        // TODO Implement NBT Handling Logic here
        // This will be similar to "/sba dev" or "/skytils dev nbt".
        if (getConfig() != null && getConfig().isLocation()) {
            LOGGER.info(TITLE + " NBT data has been copied to your clipboard!");
        }
        if (MinecraftClient.getInstance().player == null) return 0;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " NBT data copied to clipboard!").formatted(Formatting.WHITE), false);
        return Command.SINGLE_SUCCESS;
    }
    private static int dev() {
        // TODO Implement config editing & changing of the variable in config.
        LOGGER.info(TITLE + " Successfully enabled Developer mode!");
        if (MinecraftClient.getInstance().player == null) return 0;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Dev mode Enabled/Disabled" /* TODO if dev mode was true say false, vice versa.*/).formatted(Formatting.WHITE), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int location() {
        // TODO Implement config editing & changing of the variable in config.
        // TODO Make it log all location movements or make a separate toggle for that in minecraft chat
        LOGGER.info(TITLE + " Successfully enabled location logging!");
        if (MinecraftClient.getInstance().player == null) return 0;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " Location data Enabled/Disabled").formatted(Formatting.WHITE), false);
        return Command.SINGLE_SUCCESS;
    }
}
