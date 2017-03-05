package com.danny.whomi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.danny.whomi.FragmentTransitionManager;
import com.danny.whomi.GameController;
import com.danny.whomi.MyApplication;
import com.danny.whomi.R;
import com.danny.whomi.dagger.application.ApplicationComponent;
import com.danny.whomi.dagger.game.DaggerGameComponent;
import com.danny.whomi.dagger.game.GameComponent;
import com.danny.whomi.dagger.game.GameModule;
import com.danny.whomi.fragments.QuestionFragment;
import com.danny.whomi.model.objects.Player;
import com.danny.whomi.navigator.GameNavigator;

import javax.inject.Inject;

public class GameActivity extends AppCompatActivity implements GameNavigator {

    @Inject
    GameController gameController;

    private FragmentTransitionManager fragmentTransitionManager;

    private ProgressDialog progressDialog;

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

    @Override
    public void showLoading() {

        progressDialog = ProgressDialog.show(this, getString(R.string.dialog_loading_title), getString(R.string.dialog_loading_msg), true);

    }

    @Override
    public void hideLoading() {

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
