package com.example.triviatest.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        this.sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighestScore(int score){

        int currentScore = score;
        int lastScore = sharedPreferences.getInt("high_score", 0);

        if(currentScore > lastScore){
            sharedPreferences.edit().putInt("high_score", currentScore).apply();
        }
    }

    public int getHighScore(){
        return sharedPreferences.getInt("high_score", 0);
    }

    public void saveState(int index){
        sharedPreferences.edit().putInt("index", index).apply();
    }

    public int getstate(){
        return sharedPreferences.getInt("index", 0);
    }
}
