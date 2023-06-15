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
    public static String getUsername(String uuid) {
        JsonElement playerData = null;
        if (cachedData.containsValue(uuid)) {
            LOGGER.info("Using cached player data...");
            return cachedData.inverse().get(uuid);
        }
        try {
            URL userinfo = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            URLConnection urlConnect = userinfo.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));

            playerData = JsonParser.parseReader(in);

        } catch(IOException e) {
            LOGGER.info(String.valueOf(e));
        }

        cachedData.put(playerData.getAsJsonObject().get("name").getAsString(), playerData.getAsJsonObject().get("id").getAsString());

        return playerData.getAsJsonObject().get("name").getAsString();
    }

    public static String getUUID(String username) {
        JsonElement playerData = null;
        if (cachedData.containsKey(username)) {
            LOGGER.info("Using cached player data...");
            return cachedData.get(username);
        }
        try {
            URL userinfo = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            URLConnection urlConnect = userinfo.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));

            playerData = JsonParser.parseReader(in);

        } catch (IOException e) {
            LOGGER.info(String.valueOf(e));
        }

        cachedData.put(playerData.getAsJsonObject().get("name").getAsString(), playerData.getAsJsonObject().get("id").getAsString());

        return playerData.getAsJsonObject().get("id").getAsString();
    }
}
