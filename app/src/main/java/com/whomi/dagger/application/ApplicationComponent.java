package com.whomi.dagger.application;

import com.whomi.dagger.scope.PerApp;
import com.whomi.services.ClueService;
import com.whomi.services.PlayerService;
import com.whomi.services.UserService;
import com.whomi.services.WhomiAnalyticsService;

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

    WhomiAnalyticsService whomiAnalyticsService();

    PlayerService playerRepository();

    ClueService clueRepository();

    UserService userService();

}
