package io.github.skyblockcore.player;

import io.github.skyblockcore.mixin.PlayerListHudAccessor;
import io.github.skyblockcore.util.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

/**
 * Relies on {@link PlayerListHud}
 * @author Ascynx
 */
public class PlayerListAPI {
    private static final MinecraftClient client = MinecraftClient.getInstance();

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
     * @return the entirety of the Player List Hud, without limiting to 80 entries.
     * @throws IllegalStateException if the player is not initialized.
     */
    public static List<PlayerListEntry> getPlayerListEntries() {
        if (client.player == null) {
            throw new IllegalStateException("Player is not initialized");
        }
        return client.player.networkHandler.getListedPlayerListEntries().stream().sorted().toList();
    }

    /**
     * @return the tab list header see {@link PlayerListHud#header}.
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
     * @return the tab list footer see {@link PlayerListHud#footer}.
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
     * @param skipEmpty whether you should remove empty lines from the given data.
     * @return the data in the tab list, sorted into categories, whatever was not in a category is placed under UNKNOWN.
     */
    public static synchronized Map<String, List<PlayerListEntry>> getPlayerListCategories(boolean skipEmpty) {
        List<PlayerListEntry> entries = getTabList();

        Map<String, List<PlayerListEntry>> categories = new HashMap<>();
        List<String> columnNames = new ArrayList<>();

        //add UNKNOWN category
        categories.put("UNKNOWN", new ArrayList<>());

        for (PlayerListEntry entry: entries) {
            if (entry.getProfile().getName().charAt(0) != '!') {
                continue;
            }
            String key = entry.getProfile().getName().toLowerCase();

            //1 = column as char, 3 = row as char
            char column = key.charAt(1);
            int columnPos = column - 'a';
            char row = key.charAt(3);
            int rowPos = row - 'a';

            if (rowPos == 0 && entry.getDisplayName() != null) {
                //Title
                String columnKey = cleanKey(entry.getDisplayName().getString());

                columnNames.add(columnKey);
                if (!categories.containsKey(columnKey)) {
                    categories.put(columnKey, new ArrayList<>());
                    categories.get(columnKey).add(entry);
                }
            } else if (rowPos != 0) {
                if (skipEmpty && (entry.getDisplayName() == null || entry.getDisplayName().getString().trim().equals(""))) {
                    //skip if null or empty
                    continue;
                }

                //not the first one
                if (columnNames.size() <= columnPos) {
                    //no key found for column.
                    categories.get("UNKNOWN").add(entry);
                    continue;
                }
                String columnKey = columnNames.get(columnPos);
                categories.get(columnKey).add(entry);
            }
        }

        return categories;
    }

    /**
     * @param category the category you want to get the data for.
     * @return the data contained in that category, will return an empty list if none is found.
     */
    public static List<PlayerListEntry> getEntriesForCategory(String category) {
        Map<String, List<PlayerListEntry>> categories = getPlayerListCategories(true);
        String key = category.toUpperCase();

        if (categories.containsKey(key)) {
            return categories.get(key);
        }
        return List.of();
    }

    /**
     * @param orderedText the list to get the categories from.
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
                        lastKey = cleanKey(line.getString());
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
        String key = cleanKey(category);

        if (categories.containsKey(key)) {
            return categories.get(key);
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
        String key = cleanKey(category);

        if (categories.containsKey(key)) {
            return categories.get(key);
        }
        return List.of();
    }


    /**
     * @param key the key you want cleaned.
     * @return cleaned category key.
     */
    private static String cleanKey(String key) {
        key = key.trim().toUpperCase();
        key = key.replaceAll(" ", "_").replaceAll("[\\d\\W]", "");
        if (key.charAt(key.length()-1) == '_') {
            key = key.substring(0, key.length()-1);
        }
        return key;
    }
}
