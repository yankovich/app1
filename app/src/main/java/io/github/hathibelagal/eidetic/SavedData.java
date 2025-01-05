package io.github.hathibelagal.eidetic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SavedData {

    SharedPreferences prefs;
    SavedData(Activity context) {
        prefs = context.getPreferences(Context.MODE_PRIVATE);
    }

    void incrementStreak() {
        int currentStreak = prefs.getInt("STREAK", 0) + 1;
        prefs.edit().putInt("STREAK", currentStreak).apply();
    }

    void resetStreak() {
        prefs.edit().remove("STREAK").apply();
    }

    int getStreak() {
        return prefs.getInt("STREAK", 0);
    }

    boolean updateFastestTime(int time) {
        int oldFastestTime = prefs.getInt("TIME", Integer.MAX_VALUE);
        if(time < oldFastestTime) {
            prefs.edit().putInt("TIME", time).apply();
            return true;
        }
        return false;
    }

    int getFastestTime() {
        return prefs.getInt("TIME", 0);
    }

}
