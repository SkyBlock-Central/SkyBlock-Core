package io.github.skyblockcore.player;

import io.github.skyblockcore.mixin.PlayerListHudAccessor;
import java.util.List;
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
    private static final int MAX_ROW_ENTRIES = PlayerListHud.MAX_ROWS;

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
     * @return the tab list footer see {@link PlayerListHud#footer}
     * @throws IllegalStateException if InGameHud or PlayerListHud is not initialized.
     */
    public static Text getFooter() {
        if (client.inGameHud == null || client.inGameHud.getPlayerListHud() == null) {
            throw new IllegalStateException("InGameHud or Player List Hud is not initialized");
        }
        return ((PlayerListHudAccessor) client.inGameHud.getPlayerListHud()).getFooter();
    }
}
