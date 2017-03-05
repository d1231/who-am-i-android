package com.danny.whomi.dagger.question;

import com.danny.whomi.dagger.scope.PerQuestion;
import com.danny.whomi.model.objects.Player;

import dagger.Module;
import dagger.Provides;

@Module
public class QuestionModule {

    private final Player player;

    public QuestionModule(Player player) {

        this.player = player;
    }

    @PerQuestion
    @Provides
    public Player providePlayer() {

        return player;
    }

}
