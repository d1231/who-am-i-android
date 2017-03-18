package com.whomi.dagger.question;

import com.whomi.dagger.game.GameComponent;
import com.whomi.dagger.scope.PerQuestion;
import com.whomi.fragments.QuestionFragment;

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
