package com.danny.projectt.dagger.application;

import com.danny.projectt.dagger.scope.PerApp;
import com.danny.projectt.services.ClueService;
import com.danny.projectt.services.PlayerService;

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
