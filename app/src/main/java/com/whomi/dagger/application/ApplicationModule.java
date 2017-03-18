package com.whomi.dagger.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.whomi.dagger.scope.PerApp;
import com.whomi.utils.AutoValueTypeAdapterFactory;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    FirebaseAnalytics provideFirebaseAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
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

    @PerApp
    @Provides
    public Gson provideGson() {

        return new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueTypeAdapterFactory.create())
                .create();

    }

}
