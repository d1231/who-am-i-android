package com.whomi.components;

import com.whomi.services.PlayerServiceTest;

import dagger.Component;
import sharedTest.TestModule;
import sharedTest.TestScope;

@Component(
        modules = TestModule.class
)
@TestScope
public interface PlayerServiceComponent {

    void inject(PlayerServiceTest playerServiceTest);
}
