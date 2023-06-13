package io.github.skyblockcore.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class APIUtils {

    private static JsonElement PlayerInfo;
    public static String getUsername(String uuid) {
        try {
            URL userinfo = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            URLConnection urlConnect = userinfo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
            PlayerInfo = JsonParser.parseReader(in);

            in.close();
        } catch(Exception e) {
            System.out.println(e);
        }
        return PlayerInfo.getAsJsonObject().get("name").getAsString();
    }
    public static String getUUID(String username) {
        try {
            URL userinfo = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            URLConnection urlConnect = userinfo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
            PlayerInfo = JsonParser.parseReader(in);

            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return PlayerInfo.getAsJsonObject().get("id").getAsString();
    }
}
