package io.github.skyblockcore.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


import static io.github.skyblockcore.SkyBlockCore.LOGGER;

public class APIUtils {

    private static BiMap<String, String> cachedData = HashBiMap.create();
    public static String getUsername(String uuid) {
        if (cachedData.containsValue(uuid)) {
            LOGGER.info("Using cached player data...");
            return cachedData.inverse().get(uuid);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid).openConnection().getInputStream()))) {
            JsonElement playerData = JsonParser.parseReader(in);

            cachedData.put(playerData.getAsJsonObject().get("name").getAsString(), playerData.getAsJsonObject().get("id").getAsString());

            return playerData.getAsJsonObject().get("name").getAsString();
        } catch (IOException e) {
            LOGGER.info(String.valueOf(e));
        }
        return "";
    }

    public static String getUUID(String username) {
        if (cachedData.containsKey(username)) {
            LOGGER.info("Using cached player data...");
            return cachedData.get(username);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection().getInputStream()))) {
            JsonElement playerData = JsonParser.parseReader(in);

            cachedData.put(playerData.getAsJsonObject().get("name").getAsString(), playerData.getAsJsonObject().get("id").getAsString());

            return playerData.getAsJsonObject().get("id").getAsString();
        } catch (IOException e) {
            LOGGER.info(String.valueOf(e));
        }
        return "";
    }
}
