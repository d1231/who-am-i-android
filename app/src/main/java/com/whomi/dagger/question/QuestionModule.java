package com.whomi.dagger.question;

import android.content.Context;

import com.whomi.dagger.scope.PerQuestion;
import com.whomi.model.objects.Player;

import dagger.Module;
import dagger.Provides;

@Module
public class QuestionModule {

    private final Context context;
    private final Player player;

    public QuestionModule(Context context, Player player) {
        this.context = context;

        this.player = player;
    }

    @PerQuestion
    @Provides
    public Player providePlayer() {

        return player;
    }

    @PerQuestion
    @Provides
    public Context provideContext() {
        return context;
    }

}
