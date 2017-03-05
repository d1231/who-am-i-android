package com.danny.whomi.dagger.game;

import com.danny.whomi.GameController;
import com.danny.whomi.activities.GameActivity;
import com.danny.whomi.dagger.application.ApplicationComponent;
import com.danny.whomi.dagger.scope.PerGame;
import com.danny.whomi.services.ClueService;

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

    ClueService clueReposistory();

}
