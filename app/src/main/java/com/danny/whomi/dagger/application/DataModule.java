package com.danny.whomi.dagger.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.danny.whomi.dagger.scope.PerApp;
import com.danny.whomi.services.ClueService;
import com.danny.whomi.model.objects.Player;
import com.danny.whomi.utils.GsonConverter;
import com.google.gson.Gson;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    private static final String FILENAME = "player-queue";

    public DataModule() {


    }


    @PerApp
    @Provides
    public ObjectQueue<Player> providePlayerObjectQueue(Context context, Gson gson) {

        File queueFile = new File(context.getFilesDir(), FILENAME);
        FileObjectQueue.Converter<Player> converter = new GsonConverter<>(gson, Player.class);

        try {
            FileObjectQueue<Player> queue = new FileObjectQueue<>(queueFile, converter);
            return queue;
        } catch (IOException e) {
            throw new RuntimeException("Unable to create player queue");
        }


    }

    @PerApp
    @Provides
    public ClueService provideClueRepository(SharedPreferences sharedPreferences) {

        return new ClueService(sharedPreferences);

    }

}
