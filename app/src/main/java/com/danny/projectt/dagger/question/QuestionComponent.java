package com.danny.projectt.dagger.question;

import com.danny.projectt.dagger.application.ApplicationComponent;
import com.danny.projectt.dagger.game.GameComponent;
import com.danny.projectt.dagger.scope.PerQuestion;
import com.danny.projectt.fragments.QuestionFragment;

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
