package io.github.hathibelagal.eidetic;

public class LangUtils {
    public static String getHindi(int i) {
        String[] values = {
                "०", "१", "२", "३", "४", "५", "६", "७", "८", "९"
        };
        return values[i];
    }

    public static String getJapanese(int i) {
        String[] values = {
                "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
        };
        return values[i];
    }
}
