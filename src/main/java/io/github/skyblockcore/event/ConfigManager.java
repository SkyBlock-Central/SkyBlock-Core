package io.github.skyblockcore.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

import static io.github.skyblockcore.SkyBlockCore.LOGGER;
import static io.github.skyblockcore.SkyBlockCore.TITLE;

public class ConfigManager {



    private static ModConfig config;

    public static void loadConfig() {
        File configFile = getConfigFile();
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                Gson gson = new GsonBuilder().create();
                config = gson.fromJson(reader, ModConfig.class);
                if (config != null && config.isDev()) {
                    LOGGER.info(TITLE + " Config file loaded. [Dev]");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            createConfig();
            LOGGER.info(TITLE + " Config successfully created! Welcome to SkyBlockCore, a Skyblock mod designed for modern Minecraft.");
        }
    }
/*
* Config Guide ~~ Please add to this if you plan on adding config values!
* setDev: False = do not show // True = show
* setUnknownPlayer: True = hide spam // False = show spam
* setLocation: False = do not show // True = show
*/
    public static void createConfig() {
        config = new ModConfig(); // Below toggles will only change every time you wipe a config!
        config.setDev(false); // This will set all development features and logs to "TRUE". this is used for debugging errors with leaving/joining skyblock.
        config.setUnknownPlayer(true); // Don't use unless you want random UUID strings its annoying and I hated it. so now its gone!
        config.setLocation(false); // this is used for location output (see SkyBlockCore file > location stuff) this is used for debugging new locations.
        saveConfig();
    }

    public static void reloadConfig() {
        loadConfig();
    }

    public static ModConfig getConfig() {
        return config; // see io.github.skyblockcore/event/ModConfig
    }

    private static File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("SkyblockCoreConfig.json").toFile();
    }

    public static void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);
        File configFile = getConfigFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
