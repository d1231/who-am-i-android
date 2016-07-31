package com.danny.projectt.dagger.game;

import com.danny.projectt.GameController;
import com.danny.projectt.activities.GameActivity;
import com.danny.projectt.dagger.application.ApplicationComponent;
import com.danny.projectt.dagger.scope.PerGame;
import com.danny.projectt.model.ClueRepository;
import com.danny.projectt.model.ScoreRepository;

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

    ScoreRepository scoreRepository();

    ClueRepository clueReposistory();

}
