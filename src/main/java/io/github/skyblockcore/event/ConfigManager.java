package io.github.skyblockcore.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

import static io.github.skyblockcore.SkyblockCore.LOGGER;
import static io.github.skyblockcore.SkyblockCore.TITLE;

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
            LOGGER.info(TITLE + " Config successfully created! Welcome to SkyblockCore, a Skyblock mod designed for modern Minecraft.");
        }
    }

    public static void createConfig() {
        config = new ModConfig(); // Below toggles will only change every time you wipe a config!
        config.setDev(false); // This will set all development features and logs to "TRUE". this is used for debugging errors with leaving/joining skyblock.
        config.setLocation(false); // this is used for location output (see SkyblockCore file > location stuff) this is used for debugging new locations.
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
