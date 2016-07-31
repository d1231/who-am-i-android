package com.danny.projectt.dagger.main;

import com.danny.projectt.activities.MainActivity;
import com.danny.projectt.dagger.application.ApplicationComponent;
import com.danny.projectt.dagger.scope.PerMain;

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
