package io.github.skyblockcore.mixin;

import com.google.common.collect.Sets;
import io.github.skyblockcore.SkyblockCore;
import io.github.skyblockcore.event.JoinSkyblockCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ClientPlayNetworkHandler.class)
public class PlayNetworkHandlerMixin {

    private static final Set<String> SKYBLOCK_TITLES = Sets.newHashSet("SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58", "\u7A7A\u5CF6\u751F\u5B58");

    @Inject(method = "onScoreboardObjectiveUpdate", at = @At("TAIL"))
    void onScoreboardDisplay(ScoreboardObjectiveUpdateS2CPacket packet, CallbackInfo ci) {
        if (SkyblockCore.isOnSkyblock()) return;
        String header = packet.getDisplayName().getString();
        System.out.println(header);
        for (String TITLE : SKYBLOCK_TITLES) {
            if (header.contains(TITLE)) JoinSkyblockCallback.EVENT.invoker().interact();
        }
    }

}
