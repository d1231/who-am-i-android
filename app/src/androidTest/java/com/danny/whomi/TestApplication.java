package com.danny.whomi;

import com.danny.whomi.dagger.application.ApplicationComponent;
import com.danny.whomi.dagger.game.GameComponent;

public class TestApplication extends MyApplication {

    @Override
    public GameComponent getGameComponent() {
        return super.getGameComponent();
    }

    @Override
    protected ApplicationComponent createComponent() {
    }
}
