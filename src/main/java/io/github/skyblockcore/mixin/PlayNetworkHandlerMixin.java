package io.github.skyblockcore.mixin;

import io.github.skyblockcore.SkyblockCore;
import io.github.skyblockcore.event.JoinSkyblockCallback;
import io.github.skyblockcore.event.LeaveSkyblockCallback;
import io.github.skyblockcore.event.LocationChangedCallback;
import io.github.skyblockcore.util.TextUtils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "onScoreboardObjectiveUpdate", at = @At("TAIL"))
    void onScoreboardDisplay(ScoreboardObjectiveUpdateS2CPacket packet, CallbackInfo ci) {
        if (!SkyblockCore.isOnSkyblock()) return;

        Scoreboard scoreboard = this.world.getScoreboard();
        ScoreboardObjective objective = scoreboard.getNullableObjective(packet.getName());
        if (objective == null) return;

        Collection<ScoreboardPlayerScore> scores = scoreboard.getAllPlayerScores(objective);
        for (ScoreboardPlayerScore score : scores) {
            String line = Team.decorateName(scoreboard.getPlayerTeam(score.getPlayerName()), Text.literal(score.getPlayerName())).getString();
            if (line.contains("\u23E3")) {
                String location = TextUtils.stripColorCodes(line.split("\u23E3 ")[1]);
                if (location.equals(SkyblockCore.getLocation())) break;
                LocationChangedCallback.EVENT.invoker().interact(SkyblockCore.getLocation(), location);
                break;
            }
        }
    }

    private static final String SKYBLOCK_SCOREBOARD = "SBScoreboard";
    private static final String HEALTH_SCOREBOARD = "health";
    private static final String TITLE = "[SkyblockCore]";

    @Inject(method = "onScoreboardDisplay", at = @At("TAIL"))
    void onScoreboardDisplay(ScoreboardDisplayS2CPacket packet, CallbackInfo ci) {
        if (packet.getName() == null) return;
        // Firstly, Ignoring "health" as this scoreboard is a part of the Skyblock Join Process,
        // but can lead to false positives in other games.
        // In the future, this may need to be added onto, if we receive more possible false positives if Hypixel
        // adds more actively changed scoreboards.
        if (packet.getName().contains(HEALTH_SCOREBOARD)) {
            // Simple Logging Statement for testing.
            // TODO Eventually these/something similar should be a separate toggle for developers to easily debug
            boolean devModeEnabled = SkyblockCore.devModeEnabled();
            if (devModeEnabled) {
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

            boolean devModeEnabled = SkyblockCore.devModeEnabled();
            if (devModeEnabled) {
                LOGGER.info(TITLE + " Joined Skyblock [Dev Packet] > " + packet.getName());
            }
            // Simple Logging Statement for testing.
            // TODO Eventually these/something similar should be a separate toggle for developers to easily debug
            LOGGER.info(TITLE + " Joined Skyblock");
            if (!onSkyblock) JoinSkyblockCallback.EVENT.invoker().interact();
        }

        if (!packet.getName().contains(SKYBLOCK_SCOREBOARD)) {
            // Simple Logging Statement for testing.
            // TODO Eventually these/something similar should be a separate toggle for developers to easily debug
            boolean devModeEnabled = SkyblockCore.devModeEnabled();
            if (devModeEnabled) {
                LOGGER.info(TITLE + " Detected a Different Scoreboard ~ Quitting Skyblock... [Dev Packet] > " + packet.getName());
            }
            LOGGER.info(TITLE + " Leaving Skyblock...");
            if (onSkyblock) LeaveSkyblockCallback.EVENT.invoker().interact();
        }
    }
}
