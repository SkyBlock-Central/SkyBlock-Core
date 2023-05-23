package io.github.skyblockcore.mixin;

import io.github.skyblockcore.SkyblockCore;
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

import static io.github.skyblockcore.SkyblockCore.*;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayNetworkHandlerMixin {


    @Inject(method = "onTeam", at = @At("TAIL"))
    void onTeam(TeamS2CPacket packet, CallbackInfo ci) {
        // Check that the player is actually on Skyblock before processing team update
        if (!SkyblockCore.isOnSkyblock()) return;

        // SkyBlock represents lines on the scoreboard as teams
        if (packet.getTeam().isEmpty()) {
            // Team is empty, has no data for us to use.
            return;
        }
        TeamS2CPacket.SerializableTeam team = packet.getTeam().get();

        // Technically there is a player name in here, but it's not useful to us - it's just a colour code.
        String scoreboardLine = (team.getPrefix().getString() + team.getSuffix().getString()).strip() ;
        if (scoreboardLine.length() > 0 && scoreboardLine.charAt(0) == '\u23E3') {
            // This is a location line
            String location = scoreboardLine.split("\u23E3 ")[1];
            if (location.equals(SkyblockCore.getLocation())) return; // Location didn't change
            LocationChangedCallback.EVENT.invoker().interact(SkyblockCore.getLocation(), location);
        }
    }


    @Inject(method = "onScoreboardDisplay", at = @At("TAIL"))
    void onScoreboardDisplay(ScoreboardDisplayS2CPacket packet, CallbackInfo ci) {
        if (packet.getName() == null) return;
        // Firstly, Ignoring "health" as this scoreboard is a part of the Skyblock Join Process,
        // but can lead to false positives in other games.
        // In the future, this may need to be added onto, if we receive more possible false positives if Hypixel
        // adds more actively changed scoreboards.
        if (packet.getName().contains(HEALTH_SCOREBOARD)) {
            // Simple Logging Statement for developers to easily debug.
            if (getConfig() != null && getConfig().isDev()) {
                LOGGER.info(TITLE + " Detected Health Scoreboard, Safely ignored! [Dev Packet] > " + packet.getName());
            }
            return;
        }
        // Now, that "health" is out to the way we check for "SBScoreboard" on join of the world
        // However, this may also need to be added onto later if we receive more unique cases of Scoreboard names.
        // If we receive "SBScoreboard" we say we "join" Skyblock.
        // If we receive some other scoreboard name, we say we "quit" Skyblock.
        boolean onSkyblock = SkyblockCore.isOnSkyblock();
        if (packet.getName().contains(SKYBLOCK_SCOREBOARD)) {
            // Simple Logging Statement for developers to easily debug.
            if (getConfig() != null && getConfig().isDev()) {
                LOGGER.info(TITLE + " Joined Skyblock [Dev Packet] > " + packet.getName());
            }
            LOGGER.info(TITLE + " Joined Skyblock");
            if (!onSkyblock) JoinSkyblockCallback.EVENT.invoker().interact();
        }

        if (!packet.getName().contains(SKYBLOCK_SCOREBOARD)) {
            // Simple Logging Statement for developers to easily debug.
            if (getConfig() != null && getConfig().isDev()) {
                LOGGER.info(TITLE + " Detected a Different Scoreboard ~ Quitting Skyblock... [Dev Packet] > " + packet.getName());
            }
            LOGGER.info(TITLE + " Leaving Skyblock...");
            if (onSkyblock) LeaveSkyblockCallback.EVENT.invoker().interact();
        }
    }
}