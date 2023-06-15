package io.github.skyblockcore.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class APIUtils {

    private static JsonElement PlayerInfo;
    private static String Player;

    private static HashMap<String, String> PlayerData = new HashMap<String, String>();
    public static String getUsername(String uuid) {
        if (PlayerData.containsValue(uuid)) {
            for(Map.Entry<String, String> entry : PlayerData.entrySet()){
                if(Objects.equals(entry.getValue(), uuid)) {
                    Player = entry.getKey();
                }
            }
            System.out.println("Using cached player data...");
            return Player;
        }
        else {
            try {
                URL userinfo = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
                URLConnection urlConnect = userinfo.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
                PlayerInfo = JsonParser.parseReader(in);

                PlayerData.put(PlayerInfo.getAsJsonObject().get("name").getAsString(), PlayerInfo.getAsJsonObject().get("id").getAsString());

                in.close();
            } catch(Exception e) {
                System.out.println(e);
            }
            return PlayerInfo.getAsJsonObject().get("name").getAsString();
        }
    }
    public static String getUUID(String username) {
        if (PlayerData.containsKey(username)) {
            for(Map.Entry<String, String> entry : PlayerData.entrySet()){
                if(Objects.equals(entry.getKey(), username)) {
                    Player = entry.getValue();
                }
            }
            System.out.println("Using cached player data...");
            return Player;
        } else {
              try {
                  URL userinfo = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
                  URLConnection urlConnect = userinfo.openConnection();
                  BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
                  PlayerInfo = JsonParser.parseReader(in);

                  PlayerData.put(PlayerInfo.getAsJsonObject().get("name").getAsString(), PlayerInfo.getAsJsonObject().get("id").getAsString());

                  in.close();
              } catch (Exception e) {
                  System.out.println(e);
              }
            return PlayerInfo.getAsJsonObject().get("id").getAsString();
        }
    }
}
