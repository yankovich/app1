package io.github.hathibelagal.eidetic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SavedData {

    private final int MAX_STARS = 2;
    SharedPreferences prefs;

    SavedData(Activity context) {
        prefs = context.getPreferences(Context.MODE_PRIVATE);
    }

    void incrementStreak() {
        int currentStreak = prefs.getInt("STREAK", 0) + 1;
        prefs.edit().putInt("STREAK", currentStreak).apply();
    }

    int getLanguage() {
        return prefs.getInt("LANGUAGE", 0);
    }

    void setLanguage(int l) {
        prefs.edit().putInt("LANGUAGE", l).apply();
    }

    void resetStreak() {
        prefs.edit().remove("STREAK").apply();
    }

    int getStreak() {
        return prefs.getInt("STREAK", 0);
    }

    boolean updateFastestTime(int time) {
        int oldFastestTime = prefs.getInt("TIME", Integer.MAX_VALUE);
        if (time < oldFastestTime) {
            prefs.edit().putInt("TIME", time).apply();
            return true;
        }
        return false;
    }

    int getFastestTime() {
        return prefs.getInt("TIME", 0);
    }

    int getStarsAvailable() {
        return prefs.getInt("STARS", MAX_STARS);
    }

    @SuppressLint("ApplySharedPref")
    void resetStars() {
        prefs.edit().putInt("STARS", MAX_STARS).commit();
    }

    @SuppressLint("ApplySharedPref")
    void decrementStarsAvailable() {
        prefs.edit().putInt("STARS", getStarsAvailable() - 1).commit();
    }
}
