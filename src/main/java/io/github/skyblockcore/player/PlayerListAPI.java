package io.github.skyblockcore.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import io.github.skyblockcore.SkyBlockCore;
import io.github.skyblockcore.mixin.PlayerListHudAccessor;
import io.github.skyblockcore.util.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

/**
 * Relies on {@link PlayerListHud}
 * @author Ascynx
 */
public class PlayerListAPI {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final UUID NIL = new UUID(0, 0);

    /**
     * @return the first 80 entries of the player list hud (as rendered in the tab list) see {@link PlayerListHud#collectPlayerEntries()}.
     * @throws IllegalStateException if InGameHud or PlayerListHud is not initialized.
     */
    public static List<PlayerListEntry> getTabList() {
        if (client.inGameHud == null || client.inGameHud.getPlayerListHud() == null) {
            throw new IllegalStateException("InGameHud or Player List Hud is not initialized");
        }
        PlayerListHud hud = client.inGameHud.getPlayerListHud();

        return ((PlayerListHudAccessor) hud).invokeCollectPlayerEntries();
    }

    /**
     * @return the entierety of the Player List Hud, whithout limiting to 80 entries.
     * @throws IllegalStateException if player is not initialized.
     */
    public static List<PlayerListEntry> getPlayerListEntries() {
        if (client.player == null) {
            throw new IllegalStateException("player is not initialized");
        }
        return client.player.networkHandler.getListedPlayerListEntries().stream().sorted().toList();
    }

    /**
     * @return the tab list header see {@link PlayerListHud#header}
     * @throws IllegalStateException if InGameHud or PlayerListHud is not initialized.
     */
    public static Text getHeader() {
        if (client.inGameHud == null || client.inGameHud.getPlayerListHud() == null) {
            throw new IllegalStateException("InGameHud or Player List Hud is not initialized");
        }
        return ((PlayerListHudAccessor) client.inGameHud.getPlayerListHud()).getHeader();
    }

    /**
     * @return the header as a list of its siblings.
     */
    public static List<Text> getHeaderAsList() {
        Text header = getHeader();
        List<Text> siblings = header.getSiblings();
        siblings.removeIf((v) -> TextUtils.stripColorCodes(v.getString()).equals(""));
        return siblings;
    }

    /**
     * @return the tab list footer see {@link PlayerListHud#footer}
     * @throws IllegalStateException if InGameHud or PlayerListHud is not initialized.
     */
    public static Text getFooter() {
        if (client.inGameHud == null || client.inGameHud.getPlayerListHud() == null) {
            throw new IllegalStateException("InGameHud or Player List Hud is not initialized");
        }
        return ((PlayerListHudAccessor) client.inGameHud.getPlayerListHud()).getFooter();
    }

    /**
     * @return the footer as a list of its siblings.
     */
    public static List<Text> getFooterAsList() {
        Text footer = getFooter();
        List<Text> siblings = footer.getSiblings();
        siblings.removeIf((v) -> TextUtils.stripColorCodes(v.getString()).equals(""));
        return siblings;
    }

    /**
     * @return the data in the tab list, sorted into categories, whatever was not in a category is placed under UNKNOWN.
     */
    public static Map<String, List<PlayerListEntry>> getTabCategories() {
        List<PlayerListEntry> entries = getTabList();

        Map<String, List<PlayerListEntry>> categories = new HashMap<>();
        categories.put("UNKNOWN", new ArrayList<>());//whatever appears before the first category is put in UNKNOWN.
        String lastKey = "UNKNOWN";

        for (PlayerListEntry entry: entries) {
            Text displayName = entry.getDisplayName();
            if (displayName == null || displayName.getString().equalsIgnoreCase("")) continue;//skip empty entries.
            SkyBlockCore.LOGGER.info(gson.toJson(displayName));

            if (displayName.getSiblings().size() > 0) {
                //Don't know why it is in the siblings, but it is used for titles and subtitles I guess.
                if (displayName.getSiblings().get(0).getStyle().isBold()) {
                    //next category;
                    lastKey = displayName.getSiblings().get(0).getString().trim().toUpperCase();
                    categories.put(lastKey, new ArrayList<>());

                    PlayerListEntry fakeEntry = createFakeEntry();
                    fakeEntry.setDisplayName(displayName);
                    categories.get(lastKey).add(fakeEntry);

                    continue;
                } else if (displayName.getSiblings().get(0).getString().trim().equals("")) {
                    //it is on the second sibling, used for main titles like Players, Server Info, etc...
                    if (displayName.getSiblings().get(1).getStyle().isBold()) {
                        //next category;
                        lastKey = displayName.getSiblings().get(1).getString().trim().toUpperCase();
                        categories.put(lastKey, new ArrayList<>());

                        PlayerListEntry fakeEntry = createFakeEntry();
                        fakeEntry.setDisplayName(displayName);
                        categories.get(lastKey).add(fakeEntry);

                        continue;
                    }
                }
            }

            //is not a category title.
            categories.get(lastKey).add(entry);
        }

        return categories;
    }

    /**
     * @param category the category you want to get the data for.
     * @return the data contained in that category, will return an empty list if none is found.
     */
    public static List<PlayerListEntry> getEntryForCategory(String category) {
        Map<String, List<PlayerListEntry>> categories = getTabCategories();

        if (categories.containsKey(category)) {
            return categories.get(category);
        }
        return List.of();
    }

    /**
     * @param orderedText the list to get the categories from
     * @return the data in the given list, sorted into categories, whatever was not in a category is placed under UNKNOWN.
     */
    private static Map<String, List<Text>> getCategoriesFromOrderedText(List<Text> orderedText) {
        Map<String, List<Text>> categories = new HashMap<>();
        categories.put("UNKNOWN", new ArrayList<>());//whatever appears before the first category is put in UNKNOWN.
        String lastKey = "UNKNOWN";

        boolean isNextLine = false;
        for (Text line: orderedText) {
            if (isNextLine) {
                isNextLine = false;

                if (line.getSiblings().size() > 0) {
                    //Do not ask me why it is in the siblings, I do not know either.
                    if (line.getSiblings().get(0).getStyle().isBold()) {
                        //next category;
                        lastKey = line.getString().trim().toUpperCase();
                        categories.put(lastKey, new ArrayList<>());
                        continue;
                    }
                }
            }

            if (line.getString().trim().equals("")) {//since trim removes every character including the newline, we check for an empty string.
                isNextLine = true;
                continue;
            }

            categories.get(lastKey).add(line);
        }

        return categories;
    }

    /**
     * @return the data in the header, sorted into categories, whatever was not in a category is placed under UNKNOWN.
     */
    public static Map<String, List<Text>> getHeaderCategories() {
        List<Text> header = getHeaderAsList();

        return getCategoriesFromOrderedText(header);
    }

    /**
     * @param category the category you want to get the data for.
     * @return the data contained in that category, will return an empty list if none is found.
     */
    public static List<Text> getCategoryInHeader(String category) {
        Map<String, List<Text>> categories = getHeaderCategories();

        if (categories.containsKey(category)) {
            return categories.get(category);
        }
        return List.of();
    }

    /**
     * @return the data in the footer, sorted into categories, whatever was not in a category is placed under UNKNOWN.
     */
    public static Map<String, List<Text>> getFooterCategories() {
        List<Text> footer = getFooterAsList();

        return getCategoriesFromOrderedText(footer);
    }

    /**
     * @param category the category you want to get the data for.
     * @return the data contained in that category, will return an empty list if none is found.
     */
    public static List<Text> getCategoryInFooter(String category) {
        Map<String, List<Text>> categories = getFooterCategories();

        String key = category.toUpperCase();

        if (categories.containsKey(key)) {
            return categories.get(key);
        }
        return List.of();
    }



    /**
     * Create fake player list entry
     */
    public static PlayerListEntry createFakeEntry() {
        return new PlayerListEntry(new GameProfile(NIL, "fake_entry"), false);
    }

    /**
     * Dumps data about the tab list in the player's chat.
     * @return whether it succeeded or not
     */
    public static int testGetData() {
        if (!SkyBlockCore.isOnSkyblock()) {
            return 0;
        }

        assert MinecraftClient.getInstance().inGameHud != null;

        //TODO seems like it is broken here too, unknown moment.
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Tablist"));
        for (Map.Entry<String, List<PlayerListEntry>> entry: getTabCategories().entrySet()) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Texts.join(Text.of(entry.getKey()).getWithStyle(Style.EMPTY.withBold(true).withColor(Formatting.AQUA)), Text.of("")));
            for (PlayerListEntry plEntry: entry.getValue()) {
                if (plEntry.getDisplayName() != null) {
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(plEntry.getDisplayName());
                }
            }
        }

        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Headers"));
        for (Map.Entry<String, List<Text>> entry: getHeaderCategories().entrySet()) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Texts.join(Text.of(entry.getKey()).getWithStyle(Style.EMPTY.withBold(true).withColor(Formatting.AQUA)), Text.of("")));
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Texts.join(entry.getValue(), Text.of("\n")));
        }

        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Footers"));
        for (Map.Entry<String, List<Text>> entry: getFooterCategories().entrySet()) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Texts.join(Text.of(entry.getKey()).getWithStyle(Style.EMPTY.withBold(true).withColor(Formatting.AQUA)), Text.of("")));
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Texts.join(entry.getValue(), Text.of("\n")));
        }

        return 1;
    }
}
