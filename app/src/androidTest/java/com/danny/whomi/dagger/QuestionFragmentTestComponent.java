package com.danny.whomi.dagger;

import com.danny.whomi.dagger.application.ApplicationComponent;
import com.danny.whomi.fragments.QuestionFragmentTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        MockApplicationModule.class,
        MockNetworkModule.class,
        MockDataModule.class
}
)
public interface QuestionFragmentTestComponent extends ApplicationComponent {

    void inject(QuestionFragmentTest questionFragmentTest);
}
