package com.example.triviatest.model;

import androidx.annotation.NonNull;

public class Score {
    private int score;

    public Score() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return "Current Score: " + score;
    }
}
