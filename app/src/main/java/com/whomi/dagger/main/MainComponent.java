package com.whomi.dagger.main;

import com.whomi.activities.MainActivity;
import com.whomi.activities.OptionsActivity;
import com.whomi.dagger.application.ApplicationComponent;
import com.whomi.dagger.scope.PerMain;

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

    void inject(OptionsActivity optionsActivity);

}
