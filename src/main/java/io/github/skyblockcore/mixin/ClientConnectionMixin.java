package io.github.skyblockcore.mixin;

import io.github.skyblockcore.SkyBlockCore;
import io.github.skyblockcore.event.LeaveSkyblockCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "disconnect", at = @At("HEAD"))
    void onDisconnect(Text disconnectReason, CallbackInfo ci) {
        if (!SkyBlockCore.isOnSkyblock()) return;
        LeaveSkyblockCallback.EVENT.invoker().interact();
    }

}
