package com.danny.projectt.dagger.game;

import com.danny.projectt.GameController;
import com.danny.projectt.navigator.GameNavigator;
import com.danny.projectt.dagger.scope.PerGame;
import com.danny.projectt.model.PlayerRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {

    private final GameNavigator gameNavigator;

    public GameModule(GameNavigator gameNavigator) {

        this.gameNavigator = gameNavigator;

    }

    @PerGame
    @Provides
    public GameController provideGameController(PlayerRepository playerRepository, GameNavigator gameNavigator) {

        return new GameController(playerRepository, gameNavigator);

    }

    @PerGame
    @Provides
    public GameNavigator provideTransitionManager() {

        return gameNavigator;

    }

}
