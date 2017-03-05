package com.danny.whomi.dagger.main;

import com.danny.whomi.navigator.MenuNavigator;
import com.danny.whomi.dagger.scope.PerMain;

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
