package com.danny.whomi.dagger.game;

import com.danny.whomi.GameController;
import com.danny.whomi.navigator.GameNavigator;
import com.danny.whomi.dagger.scope.PerGame;
import com.danny.whomi.services.PlayerService;

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
