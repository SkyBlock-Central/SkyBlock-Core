package io.github.skyblockcore.mixin;

import io.github.skyblockcore.SkyBlockCore;
import io.github.skyblockcore.command.SkyBlockCoreCommand;
import io.github.skyblockcore.event.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.skyblockcore.SkyBlockCore.TITLE;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Shadow
    @Final
    protected ScreenHandler handler;

    /**
     * Listens for when the player presses the key to copy NBT data from an item to clipboard.
     * Also prints the item copied in chat, with hover-over showing the lore.
     *
     * @param keyCode The key code of the key that was pressed
     * @param scanCode Unused, the physical location of the key on the keyboard
     * @param modifiers The modifiers that were active when the key was pressed (e.g. shift, ctrl, caps, etc.)
     * @param cir The callback info
     */
    @Inject(method = "keyPressed", at = @At("HEAD"))
    void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        // Ensure the NBT copying functionality is enabled.
        if (!SkyBlockCoreCommand.NBTCOPYING) return;

        // Check that the player has pressed the nbt-copy key.
        if (!SkyBlockCore.copyBinding.matchesKey(keyCode, scanCode)) return;

        // Ensure there is an item to copy NBT from.
        Slot focused = this.focusedSlot;
        if (focused == null || !focused.hasStack() || !handler.getCursorStack().isEmpty()) return;
        ItemStack itemToCopyNBT = focusedSlot.getStack();
        // Ensure the player instance is not null.
        if (MinecraftClient.getInstance().player == null) return;
        // Make sure the item has NBT to copy. Send a message alerting the player if not.
        if (itemToCopyNBT.getNbt() == null) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal(TITLE + " No NBT present on item."));
            return;
        }
        // minecraft:{item_name} {nbt}
        String itemNBT = "minecraft:" + itemToCopyNBT.getItem().toString() + " " + itemToCopyNBT.getNbt();
        if (ConfigManager.getConfig() != null && ConfigManager.getConfig().isDev()) {
            // Log to console if dev mode is enabled.
            SkyBlockCore.LOGGER.info(TITLE + " [Dev NBT] > " + itemNBT);
        }
        // Copy the NBT to clipboard.
        MinecraftClient.getInstance().keyboard.setClipboard(itemNBT);

        // Send a message to the player that the NBT has been copied.
        Text chatMessage = Text.literal(TITLE + " NBT Data has been copied for: ")
                // Set text colour to white in case we change the colour of the title.
                .formatted(Formatting.WHITE).append(itemToCopyNBT.getName())
                // Add the hover-over lore (the item will be displayed when you hover over it).
                .styled(s -> s.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(itemToCopyNBT))));
        MinecraftClient.getInstance().player.sendMessage(chatMessage, false);
    }

}
