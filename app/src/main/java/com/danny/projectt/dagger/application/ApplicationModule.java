package com.danny.projectt.dagger.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.danny.projectt.dagger.scope.PerApp;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private static final String APP_PREF_NAME = "pref";

    private final Context context;

    public ApplicationModule(Context context) {

        this.context = context;
    }

    @Provides
    @PerApp
    Context provideContext() {

        return context;

    }

    @PerApp
    @Provides
    SharedPreferences sharedPreferences(Context context) {

        return context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE);
    }

}
