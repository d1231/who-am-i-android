package com.whomi.dagger.application;

import com.whomi.BuildConfig;
import com.whomi.dagger.scope.PerApp;
import com.whomi.model.network.BackendService;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Module
public class NetworkModule {

    NetworkModule() {

    }

    @PerApp
    @Provides
    public BackendService provideBackendService(Gson gson, OkHttpClient okHttpClient) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(okHttpClient)
                .build();

        return retrofit.create(BackendService.class);


    }

    @PerApp
    @Provides
    public OkHttpClient provideOkHttpClient() {

        final HttpLoggingInterceptor.Logger logger = message -> Timber.tag("OkHttp").d(message);

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger)
                .setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

    }
}
