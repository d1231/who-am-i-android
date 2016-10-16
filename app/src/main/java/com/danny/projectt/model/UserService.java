package com.danny.projectt.model;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.danny.projectt.model.network.BackendService;
import com.danny.projectt.model.objects.GameOptions;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;

public class UserService {

    private static final String PREF_MIN_YEAR = "minYear";

    private static final String PREF_NATIONS = "nations";

    private final BackendService backendService;

    private final SharedPreferences sharedPreferences;

    @Inject
    public UserService(BackendService backendService, SharedPreferences sharedPreferences) {

        this.backendService = backendService;

        this.sharedPreferences = sharedPreferences;
    }

    public Observable<List<String>> getAvailableNations() {

        return backendService.getAvailableNations();

    }

    public GameOptions getGameOptions() {

        final int minYear = sharedPreferences.getInt(PREF_MIN_YEAR, -1);

        Set<String> nations = getNations();

        return new GameOptions(minYear, nations);
    }

    @NonNull
    private Set<String> getNations() {

        Set<String> nations = sharedPreferences.getStringSet(PREF_NATIONS, Sets.newHashSet());
        nations = Sets.newHashSet(nations);
        return nations;
    }

    public void updateMinYear(int minYear) {

        sharedPreferences.edit().putInt(PREF_MIN_YEAR, minYear).apply();
    }

    public void insertNewNation(String nation) {

        final Set<String> nations = getNations();
        nations.add(nation);

        updateNations(nations);
    }

    private void updateNations(Set<String> nations) {

        sharedPreferences.edit().putStringSet(PREF_NATIONS, nations).apply();
    }

    public void removeNation(String nation) {

        final Set<String> nations = getNations();
        nations.remove(nation);

        updateNations(nations);
    }

    public void resetNations() {

        sharedPreferences.edit().remove(PREF_NATIONS).apply();

    }
}
