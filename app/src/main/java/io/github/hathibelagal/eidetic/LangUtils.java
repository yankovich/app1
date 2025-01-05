package io.github.hathibelagal.eidetic;

import java.util.HashMap;
import java.util.Objects;

public class LangUtils {

    private static final HashMap<Integer, String[]> languageMap;

    static {
        languageMap = new HashMap<>();
        languageMap.put(1, new String[]{
                "०", "१", "२", "३", "४", "५", "६", "७", "८", "९"
        });
        languageMap.put(2, new String[]{
                "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
        });
        languageMap.put(3, new String[]{
                "០", "១", "២", "៣", "៤", "៥", "៦", "៧", "៨", "៩"
        });
    }

    public static String getTranslation(int language, int i) {
        if(languageMap.containsKey(language)) {
            return Objects.requireNonNull(languageMap.get(language))[i];
        } else {
            return String.valueOf(i);
        }
    }
}
