/*
 * Copyright 2023 SkyBlock-Core Contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.skyblockcore;

import com.mojang.brigadier.CommandDispatcher;
import io.github.skyblockcore.command.SkyBlockCoreCommand;
import io.github.skyblockcore.event.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.skyblockcore.event.ConfigManager.loadConfig;

public class SkyBlockCore implements ClientModInitializer {

    public static final String ModID = "skyblockcore";
    public static final String TITLE = "[SkyBlockCore]";
    public static final String SKYBLOCK_SCOREBOARD = "SBScoreboard";
    public static final String HEALTH_SCOREBOARD = "health";
    private static boolean ON_SKYBLOCK = false;

    public static boolean isOnSkyblock() {
        return ON_SKYBLOCK;
    }

    public static String getLocation() {
        return LOCATION;
    }

    private static String LOCATION;
    public static final Logger LOGGER = LoggerFactory.getLogger(ModID);

    public static final KeyBinding copyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "skyblockcore.dev.nbtcopy",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_CONTROL,
            "skyblockcore.dev"
    ));


    @Override
    public void onInitializeClient() {
        // We Must load the config first. Otherwise, Events relying on the config such as Location do not work.
        loadConfig();
        // Install log filter

        ConsoleLogFilter.installFilter();

        ClientCommandRegistrationCallback.EVENT.register(SkyBlockCore::registerCommands);
        //ModConfig config = SkyBlockCore.getConfig();
        // example of events, TODO; move to a better class
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
            // Simple Logging Statement for mod developers to debug locations affecting their code.
            if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isLocation()) {
                LOGGER.info(TITLE + " Detected Location Change on Scoreboard! [Dev Old Location] > " + oldLocation);
                LOGGER.info(TITLE + " Detected Location Change on Scoreboard! [Dev New Location] > " + newLocation);
            }
            LOCATION = newLocation;
            return ActionResult.PASS;
        }));

    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        SkyBlockCoreCommand.register(dispatcher);
    }
}