package com.danny.projectt.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.danny.projectt.FragmentTransitionManager;
import com.danny.projectt.GameController;
import com.danny.projectt.MyApplication;
import com.danny.projectt.R;
import com.danny.projectt.dagger.application.ApplicationComponent;
import com.danny.projectt.dagger.game.DaggerGameComponent;
import com.danny.projectt.dagger.game.GameComponent;
import com.danny.projectt.dagger.game.GameModule;
import com.danny.projectt.fragments.QuestionFragment;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.navigator.GameNavigator;

import javax.inject.Inject;

public class GameActivity extends AppCompatActivity implements GameNavigator {

    @Inject
    GameController gameController;

    private FragmentTransitionManager fragmentTransitionManager;

    public static void start(Context context) {

        Intent starter = new Intent(context, GameActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_holder);

        initDependencies();

        initTransitionManager();

        gameController.startGame();

    }

    private void initDependencies() {


        final ApplicationComponent applicationComponent = MyApplication.get(this)
                                                                       .getApplicationComponent();


        final GameComponent gameComponent = DaggerGameComponent.builder()
                                                               .applicationComponent(applicationComponent)
                                                               .gameModule(new GameModule(this))
                                                               .build();

        MyApplication.get(this).setGameComponent(gameComponent);

        gameComponent.inject(this);


    }

    private void initTransitionManager() {

        fragmentTransitionManager = new FragmentTransitionManager();
        fragmentTransitionManager.takeActivity(this, R.id.content);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        gameController.finishGame();
        fragmentTransitionManager.dropActivity();

        MyApplication.get(this).setGameComponent(null);


    }

    @Override
    public void quitGame() {

        finish();

    }

    @Override
    public void showQuestion(Player player) {

        runOnUiThread(() -> {

            final QuestionFragment fragment = QuestionFragment.newInstance(player);

            fragmentTransitionManager.resetTo(fragment);

        });

    }
}
