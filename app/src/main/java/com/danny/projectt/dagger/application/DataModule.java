package com.danny.projectt.dagger.application;

import android.content.SharedPreferences;

import com.danny.projectt.dagger.scope.PerApp;
import com.danny.projectt.model.ClueRepository;
import com.danny.projectt.model.PlayerRepository;
import com.danny.projectt.model.ScoreRepository;
import com.danny.projectt.model.network.BackendService;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {


    @PerApp
    @Provides
    public PlayerRepository providePlayerRepository(BackendService backendService) {

        return new PlayerRepository(backendService);

    }


    @PerApp
    @Provides
    public ScoreRepository provideScoreRepository(SharedPreferences sharedPreferences) {

        return new ScoreRepository(sharedPreferences);

    }

    @PerApp
    @Provides
    public ClueRepository provideClueRepository(SharedPreferences sharedPreferences) {

        return new ClueRepository(sharedPreferences);

    }

}
