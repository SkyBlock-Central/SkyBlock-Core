package io.github.skyblockcore.util;

import java.util.HashMap;
import java.util.Map;

public class DungeonUtils {
    public enum DUNGEON_FLOORS { UNDEFINED("UNDEFINED"), E("E"), F1("F1"), F2("F2"), F3("F3"), F4("F4"), F5("F5"), F6("F6"), F7("F7"), M1("M1"), M2("M2"), M3("M3"), M4("M4"), M5("M5"), M6("M6"), M7("M7");
        private static final Map<String, DUNGEON_FLOORS> map = new HashMap<>();
        private final String text;

        static {
            for(DUNGEON_FLOORS floor : DUNGEON_FLOORS.values()) {
                map.put(floor.text, floor);
            }
        }

        DUNGEON_FLOORS(String text) {
            this.text = text;
        }

        public static DUNGEON_FLOORS fromString(String text) {
            return map.get(text);
        }
    }
    private static DUNGEON_FLOORS DUNGEON_FLOOR = null;

    public static DUNGEON_FLOORS getDungeonFloor() {return DUNGEON_FLOOR;}
    public static void setDungeonFloor(DUNGEON_FLOORS floor) {DUNGEON_FLOOR = floor;}
}