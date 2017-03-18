package com.whomi.dagger.main;

import com.whomi.navigator.MenuNavigator;
import com.whomi.dagger.scope.PerMain;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private MenuNavigator menuNavigator;

    public MainModule(MenuNavigator menuNavigator) {

        this.menuNavigator = menuNavigator;
    }

    @PerMain
    @Provides
    public MenuNavigator provideMenuNavigator() {

        return menuNavigator;

    }

}
