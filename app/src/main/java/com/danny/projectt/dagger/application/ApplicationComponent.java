package com.danny.projectt.dagger.application;

import com.danny.projectt.dagger.scope.PerApp;
import com.danny.projectt.model.ClueRepository;
import com.danny.projectt.model.PlayerRepository;
import com.danny.projectt.model.ScoreRepository;

import dagger.Component;

@Component(
        modules = {
                ApplicationModule.class,
                NetworkModule.class,
                DataModule.class
        }
)
@PerApp
public interface ApplicationComponent {

    PlayerRepository playerRepository();

    ScoreRepository scoreRepository();

    ClueRepository clueRepository();

}
