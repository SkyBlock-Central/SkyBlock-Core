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

    private static BiMap<String, String> CachedData = HashBiMap.create();
    public static String getUsername(String uuid) {
        JsonElement PlayerData = null;
        if (CachedData.containsValue(uuid)) {
            System.out.println("Using cached player data...");
            return CachedData.inverse().get(uuid);
        }
        try {
            URL userinfo = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            URLConnection urlConnect = userinfo.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));

            PlayerData = JsonParser.parseReader(in);

        } catch(IOException e) {
            LOGGER.info(String.valueOf(e));
        }

        CachedData.put(PlayerData.getAsJsonObject().get("name").getAsString(), PlayerData.getAsJsonObject().get("id").getAsString());

        return PlayerData.getAsJsonObject().get("name").getAsString();
    }

    public static String getUUID(String username) {
        JsonElement PlayerData = null;
        if (CachedData.containsKey(username)) {
            System.out.println("Using cached player data...");
            return CachedData.get(username);
        }
        try {
            URL userinfo = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            URLConnection urlConnect = userinfo.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));

            PlayerData = JsonParser.parseReader(in);

        } catch (IOException e) {
            LOGGER.info(String.valueOf(e));
        }

        CachedData.put(PlayerData.getAsJsonObject().get("name").getAsString(), PlayerData.getAsJsonObject().get("id").getAsString());

        return PlayerData.getAsJsonObject().get("id").getAsString();
    }
}
