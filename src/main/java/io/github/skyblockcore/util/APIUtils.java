package io.github.skyblockcore.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


import static io.github.skyblockcore.SkyBlockCore.LOGGER;

public class APIUtils {

    private static BiMap<String, String> cachedData = HashBiMap.create();
    public static String getUsername(String uuid) throws IOException {
        if (cachedData.containsValue(uuid)) {
            LOGGER.info("Using cached player data...");
            return cachedData.inverse().get(uuid);
        }
        URL userinfo = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        URLConnection urlConnect = userinfo.openConnection();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()))) {
            JsonElement playerData = JsonParser.parseReader(in);

            cachedData.put(playerData.getAsJsonObject().get("name").getAsString(), playerData.getAsJsonObject().get("id").getAsString());

            return playerData.getAsJsonObject().get("name").getAsString();
        } catch (IOException e) {
            LOGGER.info(String.valueOf(e));
        }
        return "";
    }

    public static String getUUID(String username) throws IOException {
        if (cachedData.containsKey(username)) {
            LOGGER.info("Using cached player data...");
            return cachedData.get(username);
        }

        URL userinfo = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
        URLConnection urlConnect = userinfo.openConnection();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()))) {
            JsonElement playerData = JsonParser.parseReader(in);

            cachedData.put(playerData.getAsJsonObject().get("name").getAsString(), playerData.getAsJsonObject().get("id").getAsString());

            return playerData.getAsJsonObject().get("id").getAsString();
        } catch (IOException e) {
            LOGGER.info(String.valueOf(e));
        }
        return "";
    }
}
