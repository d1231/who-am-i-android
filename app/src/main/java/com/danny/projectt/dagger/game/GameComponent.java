package com.danny.projectt.dagger.game;

import com.danny.projectt.GameController;
import com.danny.projectt.activities.GameActivity;
import com.danny.projectt.dagger.application.ApplicationComponent;
import com.danny.projectt.dagger.scope.PerGame;
import com.danny.projectt.services.ClueService;

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
