package io.github.skyblockcore.mixin;

import io.github.skyblockcore.SkyBlockCore;
import io.github.skyblockcore.event.ConfigManager;
import io.github.skyblockcore.event.JoinSkyblockCallback;
import io.github.skyblockcore.event.LeaveSkyblockCallback;
import io.github.skyblockcore.event.LocationChangedCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.skyblockcore.SkyBlockCore.*;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayNetworkHandlerMixin {


    @Inject(method = "onTeam", at = @At("TAIL"))
    void onTeam(TeamS2CPacket packet, CallbackInfo ci) {
        // Check that the player is actually on Skyblock before processing team update
        if (!SkyBlockCore.isOnSkyblock()) return;

        // SkyBlock represents lines on the scoreboard as teams
        if (packet.getTeam().isEmpty()) {
            // Team is empty, has no data for us to use.
            return;
        }
        TeamS2CPacket.SerializableTeam team = packet.getTeam().get();

        // Technically there is a player name in here, but it's not useful to us - it's just a colour code.
        String scoreboardLine = (team.getPrefix().getString() + team.getSuffix().getString()).strip();
        if (scoreboardLine.length() > 0 && scoreboardLine.charAt(0) == '\u23E3') {
            // This is a location line
            String location = scoreboardLine.split("\u23E3 ")[1];
            if (location.equals(SkyBlockCore.getLocation())) return; // Location didn't change
            LocationChangedCallback.EVENT.invoker().interact(SkyBlockCore.getLocation(), location);
        }
    }


    @Inject(method = "onScoreboardDisplay", at = @At("TAIL"))
    void onScoreboardDisplay(ScoreboardDisplayS2CPacket packet, CallbackInfo ci) {
        if (packet.getName() == null) return;
        // "health" is sent along with the first "SBScoreboard", we ignore health, as it can lead to false positives.
        // This may need to have more names added on in time, if hypixel adds more packets like this that have uses
        // in other games.
        if (packet.getName().contains(HEALTH_SCOREBOARD)) {
            // Simple Logging Statement for developers to easily debug.
            if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isDev()) {
                LOGGER.info(TITLE + " Detected Health Scoreboard, Safely ignored! [Dev Packet] > " + packet.getName());
            }
            return;
        }
        boolean onSkyblock = SkyBlockCore.isOnSkyblock();
        // If we receive a packet other than "health" or "SBScoreboard" we say the user has "left" skyblock.
        // This may need to have more names added on in time if skyblock ever removes/changes "SBScoreboard"
        if (!packet.getName().contains(SKYBLOCK_SCOREBOARD)) {
            // Simple Logging Statement for developers to easily debug.
            if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isDev()) {
                LOGGER.info(TITLE + " Detected a Different Scoreboard [Dev Packet] > " + packet.getName());
            }
            // This is done so developers keep packet info even if the event is not triggered if we are not on SB already.
            if (!onSkyblock) return;
            LOGGER.info(TITLE + " Leaving Skyblock");
            LeaveSkyblockCallback.EVENT.invoker().interact();
            return;
        }
        // Since we've eliminated all other packets, we are free to assume the user has joined Skyblock.
        // Simple Logging Statement for developers to easily debug.
        if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isDev()) {
            LOGGER.info(TITLE + " Detected Skyblock Scoreboard [Dev Packet] > " + packet.getName());
        }
        // This is done so developers keep packet info even if the event is not triggered due to already being on SB.
        if (onSkyblock) return;
        LOGGER.info(TITLE + " Joined Skyblock");
        JoinSkyblockCallback.EVENT.invoker().interact();
    }
}