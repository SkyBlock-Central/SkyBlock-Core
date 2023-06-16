package io.github.skyblockcore.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static io.github.skyblockcore.SkyBlockCore.LOGGER;

public class APIUtils {

    private static final Cache<String, String> uuidToUsernameCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();

    public static String getUsername(String uuid) {
        String cachedUsername = uuidToUsernameCache.getIfPresent(uuid);
        if (cachedUsername != null) {
            LOGGER.info("Using cached player data...");
            return cachedUsername;
        }

        try (InputStreamReader in = new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid).openConnection().getInputStream())) {
            JsonObject playerData = JsonParser.parseReader(in).getAsJsonObject();

            String username = playerData.get("name").getAsString();
            uuidToUsernameCache.put(playerData.get("id").getAsString(), username);

            return username;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public static String getUUID(String username) {
        Optional<Map.Entry<String, String>> cachedUuid = uuidToUsernameCache.asMap().entrySet().stream().filter(e -> e.getValue().equalsIgnoreCase(username)).findAny();
        if (cachedUuid.isPresent()) {
            LOGGER.info("Using cached player data...");
            return cachedUuid.get().getKey();
        }

        try (InputStreamReader in = new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection().getInputStream())) {
            JsonObject playerData = JsonParser.parseReader(in).getAsJsonObject();

            String uuid = playerData.get("id").getAsString();
            uuidToUsernameCache.put(uuid, playerData.get("name").getAsString());

            return uuid;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
