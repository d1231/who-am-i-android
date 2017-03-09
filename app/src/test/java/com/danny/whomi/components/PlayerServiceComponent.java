package com.danny.whomi.components;

import com.danny.whomi.services.PlayerServiceTest;

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
