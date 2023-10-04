package io.github.skyblockcore.dungeons;

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

        public DUNGEON_BOSSES getBoss() {
            switch(this) {
                case F1, M1: return DUNGEON_BOSSES.BONZO;
                case F2, M2: return DUNGEON_BOSSES.SCARF;
                case F3, M3: return DUNGEON_BOSSES.THE_PROFESSOR;
                case F4, M4: return DUNGEON_BOSSES.THORN;
                case F5, M5: return DUNGEON_BOSSES.LIVID;
                case F6, M6: return DUNGEON_BOSSES.SADAN;
                case F7, M7: return DUNGEON_BOSSES.WITHER_LORDS;
                default: return DUNGEON_BOSSES.UNDEFINED;
            }
        }
    }
    public enum DUNGEON_BOSSES { UNDEFINED, BONZO, SCARF, THE_PROFESSOR, THORN, LIVID, SADAN, WITHER_LORDS }
    public enum DUNGEON_CLASSES { HEALER, MAGE, BERSERK, ARCHER, TANK }

    private static DUNGEON_FLOORS DUNGEON_FLOOR = null;
    private static DUNGEON_BOSSES DUNGEON_BOSS = null;

    public static DUNGEON_FLOORS getDungeonFloor() {return DUNGEON_FLOOR;}
    public static void setDungeonFloor(DUNGEON_FLOORS floor) {DUNGEON_FLOOR = floor;}
    public static DUNGEON_BOSSES getDungeonBoss() {return DUNGEON_BOSS;}
    public static void setDungeonBoss(DUNGEON_BOSSES boss) {DUNGEON_BOSS = boss;}
}