package com.danny.whomi.dagger.main;

import com.danny.whomi.activities.MainActivity;
import com.danny.whomi.dagger.application.ApplicationComponent;
import com.danny.whomi.dagger.scope.PerMain;

import dagger.Component;

@PerMain
@Component(
        dependencies = {
                ApplicationComponent.class
        },
        modules = {
                MainModule.class
        }
)
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
