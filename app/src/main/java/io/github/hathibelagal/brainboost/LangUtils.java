/* Copyright 2024 Ashraff Hathibelagal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hathibelagal.brainboost;

import java.util.HashMap;
import java.util.Objects;

public class LangUtils {

    private static final HashMap<Integer, String[]> languageMap;

    static {
        languageMap = new HashMap<>();
        languageMap.put(1, new String[]{"०", "१", "२", "३", "४", "५", "६", "७", "८", "९"});
        languageMap.put(2, new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"});
        languageMap.put(3, new String[]{"០", "១", "២", "៣", "៤", "៥", "៦", "៧", "៨", "៩"});
    }

    public static String getTranslation(int language, int i) {
        if (languageMap.containsKey(language)) {
            return Objects.requireNonNull(languageMap.get(language))[i];
        } else {
            return String.valueOf(i);
        }
    }
}
