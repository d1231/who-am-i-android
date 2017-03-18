package com.whomi.services;

import com.whomi.model.network.BackendService;
import com.whomi.model.objects.GameOptions;
import com.google.common.collect.Maps;

import java.util.Map;
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


    public Observable<Map<String, Boolean>> getNationsStatus() {

        Set<String> allNations = persistenceService.getAllNationsPref();
        Set<String> selectedNations = persistenceService.getSelectedNationsPref();

        return backendService.getAvailableNations()
                .doOnNext(persistenceService::setNations)
                .map(nations -> {

                    Map<String, Boolean> retVal = Maps.newHashMap();

                    for (String nation : nations) {
                        if (!allNations.contains(nation)) {
                            retVal.put(nation, true);
                            insertNewNation(nation);
                        } else if (selectedNations.contains(nation)) {
                            retVal.put(nation, true);
                        } else {
                            retVal.put(nation, false);
                        }
                    }

                    return retVal;

                });

    }


    public GameOptions getGameOptions() {

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

    public Set<String> getSelectedNations() {
        return persistenceService.getSelectedNationsPref();
    }
}
