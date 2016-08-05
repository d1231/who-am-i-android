package com.danny.projectt.dagger.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.danny.projectt.dagger.scope.PerApp;
import com.danny.projectt.model.ClueRepository;
import com.danny.projectt.model.ScoreRepository;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.utils.GsonConverter;
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
    public ScoreRepository provideScoreRepository(SharedPreferences sharedPreferences) {

        return new ScoreRepository(sharedPreferences);

    }

    @PerApp
    @Provides
    public ClueRepository provideClueRepository(SharedPreferences sharedPreferences) {

        return new ClueRepository(sharedPreferences);

    }

}
