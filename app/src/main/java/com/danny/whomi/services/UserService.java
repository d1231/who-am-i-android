package com.danny.whomi.services;

import com.danny.whomi.model.network.BackendService;
import com.danny.whomi.model.objects.GameOptions;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;

public class UserService {


    private final BackendService backendService;

    private final PersistenceService persistenceService;

    @Inject
    public UserService(BackendService backendService, PersistenceService persistenceService) {

        this.backendService = backendService;

        this.persistenceService = persistenceService;
    }

    public Observable<List<String>> getAvailableNations() {

        return backendService.getAvailableNations();

    }

    GameOptions getGameOptions() {

        final int minYear = persistenceService.getMinYearPref();

        Set<String> nations = persistenceService.getSelectedNationsPref();

        return new GameOptions(minYear, nations);
    }


    public void updateMinYear(int minYear) {

        persistenceService.setMinYearPref(minYear);
    }

    public void insertNewNation(String nation) {

        persistenceService.insertNewNationsToSelectedPref(nation);

    }


    public void removeNation(String nation) {

        persistenceService.deleteNationsToSelectedPref(nation);

    }

    public void resetNations() {

        persistenceService.removeAllSelectedNations();

    }
}
