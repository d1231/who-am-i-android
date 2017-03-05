package com.danny.whomi.components;

import com.danny.whomi.PlayerServiceTest;
import com.danny.whomi.dagger.main.MainComponent;
import com.danny.whomi.services.PlayerService;

import dagger.Component;

@Component(
        modules = TestModule.class
)
public interface PlayerServiceComponent {

    void inject(PlayerServiceTest playerServiceTest);
}
