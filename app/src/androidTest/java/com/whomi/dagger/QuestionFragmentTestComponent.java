package com.whomi.dagger;

import com.whomi.dagger.application.ApplicationComponent;
import com.whomi.fragments.QuestionFragmentTest;

import dagger.Component;
import sharedTest.TestModule;
import sharedTest.TestScope;

@Component(modules = {
        TestModule.class
}
)
@TestScope
public interface QuestionFragmentTestComponent extends ApplicationComponent {

    void inject(QuestionFragmentTest questionFragmentTest);
}
