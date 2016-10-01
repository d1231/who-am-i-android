package com.danny.projectt;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.danny.projectt.dagger.application.ApplicationComponent;
import com.danny.projectt.dagger.application.ApplicationModule;
import com.danny.projectt.dagger.application.DaggerApplicationComponent;
import com.danny.projectt.dagger.game.GameComponent;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MyApplication extends Application {

    private ApplicationComponent applicationComponent;

    private GameComponent gameComponent;

    private RefWatcher refWatcher;

    public static MyApplication get(Context context) {

        return ((MyApplication) context.getApplicationContext());
    }

    public RefWatcher getRefWatcher() {

        return refWatcher;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        if (BuildConfig.REPORT_CRASHES) {
            setupCrashlytics();
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        refWatcher = LeakCanary.install(this);

        applicationComponent = createComponent();

    }

    private void setupCrashlytics() {

        final Crashlytics crashlytics = new Crashlytics();

        Fabric.with(getApplicationContext(), crashlytics);

        Crashlytics.setString("GIT_SHA_KEY", BuildConfig.GIT_SHA);
        Crashlytics.setString("BUILD_TIME", BuildConfig.BUILD_TIME);

    }

    protected ApplicationComponent createComponent() {

        return DaggerApplicationComponent.builder()
                                         .applicationModule(new ApplicationModule(getApplicationContext()))
                                         .build();
    }

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);

        MultiDex.install(this);

    }

    public ApplicationComponent getApplicationComponent() {

        return applicationComponent;
    }

    public GameComponent getGameComponent() {

        return gameComponent;
    }

    public void setGameComponent(GameComponent gameComponent) {

        this.gameComponent = gameComponent;
    }
}
