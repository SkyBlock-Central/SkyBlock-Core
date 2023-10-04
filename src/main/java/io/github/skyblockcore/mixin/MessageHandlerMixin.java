package io.github.skyblockcore.mixin;

import io.github.skyblockcore.event.dungeons.*;
import io.github.skyblockcore.util.TextUtils;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.skyblockcore.SkyBlockCore.*;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {
    @Inject(method = "onGameMessage", at = @At("HEAD"))
    void onGameMessage(Text messageText, boolean overlay, CallbackInfo ci) {
        String message = TextUtils.stripColorCodes(messageText.getString());

        if(!isInDungeon()) return;

        // Dungeon Started
        if(message.contains("Starting in 1 second.")) {
            LOGGER.info(TITLE + " Dungeon Started!");
            DungeonStartedCallback.EVENT.invoker().interact();
        }

        // Dungeon Ended
        Pattern pattern = Pattern.compile("\\+\\d+(\\.\\d+)?\\s+Catacombs Experience");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find()) {
            LOGGER.info(TITLE + " Dungeon Ended!");
            DungeonEndedCallback.EVENT.invoker().interact();
        }

        // Wither Key Obtained
        if(message.contains(" has obtained Wither Key!")) {
            Pattern pattern2 = Pattern.compile("(?:\\[(\\w+\\+*\\]*)\\s*)?(\\w+)\\s+has");
            Matcher matcher2 = pattern2.matcher(message);
            if(matcher2.find()) {
                String username = matcher2.group(2);
                LOGGER.info(TITLE + " Wither Key Obtained By " + username + "!");
                WitherKeyObtainedCallback.EVENT.invoker().interact(username);
            } else {
                LOGGER.info(TITLE + " Wither Key Obtained! (But The Username of the Obtainer Is Not Found)");
                WitherKeyObtainedCallback.EVENT.invoker().interact(null);
            }
        }

        // Wither Door Opened
        if(message.contains(" opened a WITHER door!")) {
            Pattern pattern3 = Pattern.compile("(?:\\[(\\w+\\+*\\]*)\\s*)?(\\w+)\\s+opened");
            Matcher matcher3 = pattern3.matcher(message);
            if(matcher3.find()) {
                String username = matcher3.group(2);
                LOGGER.info(TITLE + " Wither Door Opened By " + username + "!");
                WitherDoorOpenedCallback.EVENT.invoker().interact(username);
            }
        }

        // Blood Key Obtained
        if(message.contains(" has obtained Blood Key!")) {
            Pattern pattern4 = Pattern.compile("(?:\\[(\\w+\\+*\\]*)\\s*)?(\\w+)\\s+has");
            Matcher matcher4 = pattern4.matcher(message);
            if(matcher4.find()) {
                String username = matcher4.group(2);
                LOGGER.info(TITLE + " Blood Key Obtained By " + username + "!");
                BloodKeyObtainedCallback.EVENT.invoker().interact(username);
            }
        }

        // Blood Door Opened
        if(message.contains("The BLOOD DOOR has been opened!")) {
            LOGGER.info(TITLE + " Blood Door Opened!");
            BloodDoorOpenedCallback.EVENT.invoker().interact();
        }

        if(message.contains("[BOSS] ")) {
            LOGGER.info(TITLE + " Entered Boss Fight!");
            EnteredBossfightCallback.EVENT.invoker().interact();
        }
    }
}
