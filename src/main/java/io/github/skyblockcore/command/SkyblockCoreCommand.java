package io.github.skyblockcore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static io.github.skyblockcore.SkyblockCore.LOGGER;
import static io.github.skyblockcore.SkyblockCore.loadConfig;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class SkyblockCoreCommand {

    private static final String TITLE = "[SkyblockCore]";


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
        return Command.SINGLE_SUCCESS;
    }

    private static int nbt() {
        // TODO Implement NBT Handling Logic here
        //  This will be similar to "/sba dev" or "/skytils dev nbt".
        LOGGER.info(TITLE + " This would of started/stopped the NBT data.");
        return Command.SINGLE_SUCCESS;
    }

    private static int dev() {
        // TODO Implement config editing & changing of the variable in config.
        LOGGER.info(TITLE + " This would of started/stopped showing developer data.");
        return Command.SINGLE_SUCCESS;
    }

    private static int location() {
        // TODO Implement config editing & changing of the variable in config.
        LOGGER.info(TITLE + " This would of started/stopped showing developer location data.");
        return Command.SINGLE_SUCCESS;
    }
}
