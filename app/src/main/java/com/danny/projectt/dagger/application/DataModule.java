package com.danny.projectt.dagger.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.danny.projectt.dagger.scope.PerApp;
import com.danny.projectt.model.ClueRepository;
import com.danny.projectt.model.PlayerRepository;
import com.danny.projectt.model.ScoreRepository;
import com.danny.projectt.model.database.RealmInteractor;
import com.danny.projectt.model.network.BackendService;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    public DataModule() {


    }


    @PerApp
    @Provides
    public PlayerRepository providePlayerRepository(BackendService backendService, RealmInteractor realmInteractor) {

        return new PlayerRepository(backendService, realmInteractor);

    }

    @Provides
    public RealmInteractor provideRealmInteractor(Context context) {

        return new RealmInteractor(context);

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
