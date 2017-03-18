package com.whomi.services;

import android.content.SharedPreferences;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

public class ClueService {

    private static final String PREF_CLUE = "clues";

    private static final String PREF_CLUE_BONUS = "clue_bonus";

    private int clues;

    private SharedPreferences sharedPreferences;

    private BehaviorSubject<Integer> cluesSubject;

    @Inject
    public ClueService(SharedPreferences sharedPreferences) {

        this.sharedPreferences = sharedPreferences;
        this.clues = sharedPreferences.getInt(PREF_CLUE, 25);

        cluesSubject = BehaviorSubject.create(this.clues);
    }

    public void clueUsed() {

        Timber.d("clueUsed() called with: clues=[" + clues + "]");

        clues = Math.max(0, clues - 1);

        sharedPreferences.edit().putInt(PREF_CLUE, clues).apply();

        cluesSubject.onNext(clues);
    }

    public void addClues(int clueNum) {

        Timber.d("addClues() called with: " + "clueNum = [" + clueNum + "], clues=[" + clues + "]");

        clues += clueNum;

        sharedPreferences.edit().putInt(PREF_CLUE, clues).putLong(PREF_CLUE_BONUS, System.currentTimeMillis()).apply();

        cluesSubject.onNext(clues);
    }

    public int getClues() {

        return clues;

    }

    public long lastClueBonusTimestamp() {

        return sharedPreferences.getLong(PREF_CLUE_BONUS, 0);

    }

    public Observable<Integer> getCluesObservable() {

        return cluesSubject;
    }

}
