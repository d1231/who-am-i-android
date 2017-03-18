package com.whomi;

import com.whomi.dagger.DaggerQuestionFragmentTestComponent;
import com.whomi.dagger.application.ApplicationComponent;
import com.whomi.dagger.game.GameComponent;

import sharedTest.TestModule;

public class TestApplication extends WhomiApplication {

    @Override
    public GameComponent getGameComponent() {
        return super.getGameComponent();
    }

    @Override
    protected ApplicationComponent createComponent() {

        return DaggerQuestionFragmentTestComponent.builder().testModule(new TestModule()).build();

    }
}
