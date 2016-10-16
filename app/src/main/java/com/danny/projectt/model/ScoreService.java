package com.danny.projectt.model;

import android.content.SharedPreferences;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ScoreService {

    static final String PREF_SCORE = "score";

    static final String PREF_SEQUENCE = "sequence";

    private final SharedPreferences sharedPreferences;

    private int currentSequence;

    private int totalScore;


    private BehaviorSubject<Integer> scoreSubject = BehaviorSubject.create();

    public ScoreService(SharedPreferences sharedPreferences) {

        this.sharedPreferences = sharedPreferences;
        this.totalScore = sharedPreferences.getInt(PREF_SCORE, 0);
        this.currentSequence = sharedPreferences.getInt(PREF_SEQUENCE, 0);

        scoreSubject.onNext(totalScore); // setup default

    }

    public void addQuestionScore(int score) {

        totalScore += score;

        sharedPreferences.edit().putInt(PREF_SCORE, totalScore).apply();

        scoreSubject.onNext(totalScore);

    }

    public Observable<Integer> getTotalScoreObservable() {

        return scoreSubject;
    }


    public void setSequence(int currentSequence) {

        this.currentSequence = currentSequence;

        sharedPreferences.edit().putInt(PREF_SEQUENCE, currentSequence).apply();

    }

    public int getCurrentSequence() {

        return currentSequence;
    }
}
