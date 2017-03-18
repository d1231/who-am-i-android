package com.whomi.dagger.game;

import com.whomi.presenters.GameController;
import com.whomi.navigator.GameNavigator;
import com.whomi.dagger.scope.PerGame;
import com.whomi.services.PlayerService;

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
    public GameController provideGameController(PlayerService playerService, GameNavigator gameNavigator) {

        return new GameController(playerService, gameNavigator);

    }

    @PerGame
    @Provides
    public GameNavigator provideTransitionManager() {

        return gameNavigator;

    }

}
