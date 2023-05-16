package io.github.skyblockcore.mixin;

import com.google.common.collect.Sets;
import io.github.skyblockcore.SkyblockCore;
import io.github.skyblockcore.event.JoinSkyblockCallback;
import io.github.skyblockcore.event.LeaveSkyblockCallback;
import io.github.skyblockcore.event.LocationChangedCallback;
import io.github.skyblockcore.util.TextUtils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayNetworkHandlerMixin {

    @Shadow private ClientWorld world;
    private static final Set<String> SKYBLOCK_TITLES = Sets.newHashSet("SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58");

    @Inject(method = "onScoreboardObjectiveUpdate", at = @At("TAIL"))
    void onScoreboardDisplay(ScoreboardObjectiveUpdateS2CPacket packet, CallbackInfo ci) {
        boolean onSkyblock = SkyblockCore.isOnSkyblock();
        Scoreboard scoreboard = this.world.getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID);
        if (objective == null) return;
        String header = objective.getDisplayName().getString();
        for (String TITLE : SKYBLOCK_TITLES) {
            if (header.contains(TITLE)) {
                if (!onSkyblock) JoinSkyblockCallback.EVENT.invoker().interact();
                return;
            }
        }
        if (onSkyblock) LeaveSkyblockCallback.EVENT.invoker().interact();
    }

    @Inject(method = "onScoreboardObjectiveUpdate", at = @At("TAIL"))
    void onScoreboardObjective(ScoreboardObjectiveUpdateS2CPacket packet, CallbackInfo ci) {
        if (!SkyblockCore.isOnSkyblock()) return;
        Scoreboard scoreboard = this.world.getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjectiveForSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID);
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

}
