package com.danny.whomi.dagger.question;

import com.danny.whomi.dagger.game.GameComponent;
import com.danny.whomi.dagger.scope.PerQuestion;
import com.danny.whomi.fragments.QuestionFragment;

import dagger.Component;

@PerQuestion
@Component(
        modules = {
                QuestionModule.class
        },
        dependencies = {
                GameComponent.class
        }
)
public interface QuestionComponent {

    void inject(QuestionFragment questionFragment);

}
