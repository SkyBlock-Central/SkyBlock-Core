package io.github.skyblockcore.util;

public class TextUtils {

    public static String stripColorCodes(String text) {
        if (!text.contains("\u00A7")) return text;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\u00A7') i++;
            else sb.append(c);
        }
        return sb.toString();
    }

}
