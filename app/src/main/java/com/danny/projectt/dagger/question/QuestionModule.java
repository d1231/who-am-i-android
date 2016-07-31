package com.danny.projectt.dagger.question;

import com.danny.projectt.dagger.scope.PerQuestion;
import com.danny.projectt.model.objects.Player;

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
