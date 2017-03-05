package com.danny.whomi.dagger.application;

import com.danny.whomi.dagger.scope.PerApp;
import com.danny.whomi.services.ClueService;
import com.danny.whomi.services.PlayerService;

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

    PlayerService playerRepository();

    ClueService clueRepository();

}
