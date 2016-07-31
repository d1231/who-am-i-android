package com.danny.projectt.dagger.main;

import com.danny.projectt.navigator.MenuNavigator;
import com.danny.projectt.dagger.scope.PerMain;

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
