package com.danny.whomi.services;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.danny.whomi.model.objects.Guess;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

class PersistenceService {

    private static final String PREF_MIN_YEAR = "minYear";

    private static final String PREF_NATIONS = "nations";

    private static final String PREF_GUESS_PLAYER_ID = "currentPlayerId";

    private static final String PREF_GUESS_CURRENT = "currentGuess";

    private static final String PREF_GUESS_NAME = "currentGuessPlayerName";


    private final SharedPreferences sharedPreferences;

    @Inject
    public PersistenceService(SharedPreferences sharedPreferences) {

        this.sharedPreferences = sharedPreferences;
    }

    int getMinYearPref() {

        return sharedPreferences.getInt(PREF_MIN_YEAR, -1);

    }

    void setMinYearPref(int minYearPref) {

        sharedPreferences.edit().putInt(PREF_MIN_YEAR, minYearPref).apply();
    }

    void insertNewNationsToSelectedPref(String... newNations) {

        final Set<String> nationsPref = getSelectedNationsPref();

        Collections.addAll(nationsPref, newNations);

        updateNations(nationsPref);
    }

    @NonNull
    Set<String> getSelectedNationsPref() {

        Set<String> nations = sharedPreferences.getStringSet(PREF_NATIONS, Sets.newHashSet());
        nations = Sets.newHashSet(nations);
        return nations;
    }

    private void updateNations(Set<String> nations) {

        sharedPreferences.edit().putStringSet(PREF_NATIONS, nations).apply();
    }

    void deleteNationsToSelectedPref(String... deletedNations) {


        final Set<String> nationsPref = getSelectedNationsPref();

        for (String nation : deletedNations) {

            nationsPref.remove(nation);
        }

        updateNations(nationsPref);
    }

    void removeAllSelectedNations() {

        sharedPreferences.edit().remove(PREF_NATIONS).apply();

    }


    Guess getCurrentGuess() {

        final String playerId = sharedPreferences.getString(PREF_GUESS_PLAYER_ID, "");
        final String playerName = sharedPreferences.getString(PREF_GUESS_NAME, "");
        final String guess = sharedPreferences.getString(PREF_GUESS_CURRENT, "");

        return new Guess(playerId, playerName, guess);
    }

    void setCurrentGuess(Guess guess) {

        sharedPreferences.edit()
                         .putString(PREF_GUESS_PLAYER_ID, guess.getPlayerId())
                         .putString(PREF_GUESS_NAME, guess.getPlayerName())
                         .putString(PREF_GUESS_CURRENT, guess.getGuess())
                         .apply();

    }
}
