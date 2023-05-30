package io.github.skyblockcore.mixin;

import io.github.skyblockcore.SkyblockCore;
import io.github.skyblockcore.command.SkyblockCoreCommand;
import io.github.skyblockcore.event.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.skyblockcore.SkyblockCore.TITLE;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Shadow @Nullable protected Slot focusedSlot;

    @Shadow @Final protected ScreenHandler handler;

    @Inject(method = "keyPressed", at = @At("HEAD"))
    void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!SkyblockCoreCommand.NBTCOPYING) return;
        if (GLFW.GLFW_KEY_RIGHT_CONTROL != keyCode) return;
        Slot focused = this.focusedSlot;
        if (focused == null || !focused.hasStack() || !handler.getCursorStack().isEmpty()) return;
        ItemStack itemToCopyNBT = focusedSlot.getStack();
        if (itemToCopyNBT.getNbt() == null) return;
        String itemNBT = "minecraft:" + itemToCopyNBT.getItem().toString() + " " + itemToCopyNBT.getNbt();
        if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isDev()) SkyblockCore.LOGGER.info(TITLE + " [Dev NBT] > " + itemNBT);
        MinecraftClient.getInstance().keyboard.setClipboard(itemNBT);
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " NBT Data has been copied for: ").formatted(Formatting.WHITE).append(itemToCopyNBT.getName()), false);
    }

}
