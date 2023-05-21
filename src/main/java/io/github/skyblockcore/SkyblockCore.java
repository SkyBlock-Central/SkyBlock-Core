/*
 * Copyright 2023 SkyBlock-Core Contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.skyblockcore;

import io.github.skyblockcore.event.JoinSkyblockCallback;
import io.github.skyblockcore.event.LeaveSkyblockCallback;
import io.github.skyblockcore.event.LocationChangedCallback;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockCore implements ClientModInitializer {
    public static final String ModID = "skyblockcore";

    private static boolean ON_SKYBLOCK = false;
    public static boolean isOnSkyblock() { return ON_SKYBLOCK; }
    public static String getLocation() { return LOCATION; }
    private static String LOCATION;

    public static final Logger LOGGER = LoggerFactory.getLogger(ModID);

    @Override
    public void onInitializeClient() {
        // example of events, TODO; move to better class
        JoinSkyblockCallback.EVENT.register(() -> {
            ON_SKYBLOCK = true;
            return ActionResult.PASS;
        });
        LeaveSkyblockCallback.EVENT.register(() -> {
            ON_SKYBLOCK = false;
            LOCATION = null;
            return ActionResult.PASS;
        });
        LocationChangedCallback.EVENT.register(((oldLocation, newLocation) -> {
            LOCATION = newLocation;
            return ActionResult.PASS;
        }));
    }

}
