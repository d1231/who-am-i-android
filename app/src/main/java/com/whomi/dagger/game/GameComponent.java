package com.whomi.dagger.game;

import com.whomi.presenters.GameController;
import com.whomi.activities.GameActivity;
import com.whomi.dagger.application.ApplicationComponent;
import com.whomi.dagger.scope.PerGame;
import com.whomi.services.ClueService;
import com.whomi.services.WhomiAnalyticsService;

import dagger.Component;

@PerGame
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                GameModule.class
        }
)
public interface GameComponent {

    void inject(GameActivity gameActivity);

    GameController gameController();

    ClueService clueRepository();

    WhomiAnalyticsService whomiAnalyticsService();

}
