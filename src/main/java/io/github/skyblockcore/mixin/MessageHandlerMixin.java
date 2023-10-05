package io.github.skyblockcore.mixin;

import io.github.skyblockcore.Dungeons;
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

        // Dungeons
        if(isInDungeon()) {
            if(message.contains("[Healer] Ghost Healing Aura Healing")) {
                DUNGEON_CLASS = Dungeons.DUNGEON_CLASSES.HEALER;
                LOGGER.info(TITLE + " Dungeon Started! Class: " + DUNGEON_CLASS);
                DungeonStartedCallback.EVENT.invoker().interact(DUNGEON_CLASS);
            } else if(message.contains("[Mage] Intelligence")) {
                DUNGEON_CLASS = Dungeons.DUNGEON_CLASSES.MAGE;
                LOGGER.info(TITLE + " Dungeon Started! Class: " + DUNGEON_CLASS);
                DungeonStartedCallback.EVENT.invoker().interact(DUNGEON_CLASS);
            } else if(message.contains("[Berserk] Lust For Blood Damage Increase Per Hit")) {
                DUNGEON_CLASS = Dungeons.DUNGEON_CLASSES.BERSERK;
                LOGGER.info(TITLE + " Dungeon Started! Class: " + DUNGEON_CLASS);
                DungeonStartedCallback.EVENT.invoker().interact(DUNGEON_CLASS);
            } else if(message.contains("[Archer] Extra Arrow Chance")) {
                DUNGEON_CLASS = Dungeons.DUNGEON_CLASSES.ARCHER;
                LOGGER.info(TITLE + " Dungeon Started! Class: " + DUNGEON_CLASS);
                DungeonStartedCallback.EVENT.invoker().interact(DUNGEON_CLASS);
            } else if(message.contains("[Tank] Absorption Shield Health Required")) {
                DUNGEON_CLASS = Dungeons.DUNGEON_CLASSES.TANK;
                LOGGER.info(TITLE + " Dungeon Started! Class: " + DUNGEON_CLASS);
                DungeonStartedCallback.EVENT.invoker().interact(DUNGEON_CLASS);
            }

            // Dungeon Ended
            Pattern pattern = Pattern.compile("Team Score: (\\d+)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                int score = Integer.parseInt(matcher.group(1));
                LOGGER.info(TITLE + " Dungeon Ended! Score: " + score);
                DungeonEndedCallback.EVENT.invoker().interact(score);
            }

            // Wither Key Obtained
            if (message.contains(" has obtained Wither Key!")) {
                Pattern pattern2 = Pattern.compile("(?:\\[(\\w+\\+*\\]*)\\s*)?(\\w+)\\s+has");
                Matcher matcher2 = pattern2.matcher(message);
                if (matcher2.find()) {
                    String username = matcher2.group(2);
                    LOGGER.info(TITLE + " Wither Key Obtained By " + username + "!");
                    WitherKeyObtainedCallback.EVENT.invoker().interact(username);
                } else {
                    LOGGER.info(TITLE + " Wither Key Obtained! (But The Username of the Obtainer Is Not Found)");
                    WitherKeyObtainedCallback.EVENT.invoker().interact(null);
                }
            }

            // Wither Door Opened
            if (message.contains(" opened a WITHER door!")) {
                Pattern pattern3 = Pattern.compile("(?:\\[(\\w+\\+*\\]*)\\s*)?(\\w+)\\s+opened");
                Matcher matcher3 = pattern3.matcher(message);
                if (matcher3.find()) {
                    String username = matcher3.group(2);
                    LOGGER.info(TITLE + " Wither Door Opened By " + username + "!");
                    WitherDoorOpenedCallback.EVENT.invoker().interact(username);
                }
            }

            // Blood Key Obtained
            if (message.contains(" has obtained Blood Key!")) {
                Pattern pattern4 = Pattern.compile("(?:\\[(\\w+\\+*\\]*)\\s*)?(\\w+)\\s+has");
                Matcher matcher4 = pattern4.matcher(message);
                if (matcher4.find()) {
                    String username = matcher4.group(2);
                    LOGGER.info(TITLE + " Blood Key Obtained By " + username + "!");
                    BloodKeyObtainedCallback.EVENT.invoker().interact(username);
                }
            }

            // Blood Door Opened
            if (message.contains("The BLOOD DOOR has been opened!")) {
                LOGGER.info(TITLE + " Blood Door Opened!");
                BloodDoorOpenedCallback.EVENT.invoker().interact();
            }

            if (message.contains("[BOSS] ") && !message.contains("[BOSS] The Watcher:")) {
                Dungeons.DUNGEON_BOSSES boss = Dungeons.getDungeonFloor().getBoss();
                if(!ENTERED_BOSSFIGHT) {
                    EnteredBossfightCallback.EVENT.invoker().interact(boss);
                    LOGGER.info(TITLE + " Entered Boss Fight! Boss: " + boss);
                    ENTERED_BOSSFIGHT = true;
                }
            }
        }
    }
}