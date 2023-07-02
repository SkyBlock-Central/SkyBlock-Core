package io.github.skyblockcore.mixin;

import java.util.List;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerListHud.class)
public interface PlayerListHudAccessor {

    /**
     * invokes {@link PlayerListHud#collectPlayerEntries()};
     */
    @Invoker
    List<PlayerListEntry> invokeCollectPlayerEntries();

    @Accessor
    Text getHeader();

    @Accessor
    Text getFooter();
}
