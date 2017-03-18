package com.whomi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.whomi.FragmentTransitionManager;
import com.whomi.presenters.GameController;
import com.whomi.WhomiApplication;
import com.whomi.R;
import com.whomi.dagger.application.ApplicationComponent;
import com.whomi.dagger.game.DaggerGameComponent;
import com.whomi.dagger.game.GameComponent;
import com.whomi.dagger.game.GameModule;
import com.whomi.fragments.QuestionFragment;
import com.whomi.model.objects.Player;
import com.whomi.navigator.GameNavigator;

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


        final ApplicationComponent applicationComponent = WhomiApplication.get(this)
                .getApplicationComponent();


        final GameComponent gameComponent = DaggerGameComponent.builder()
                .applicationComponent(applicationComponent)
                .gameModule(new GameModule(this))
                .build();

        WhomiApplication.get(this).setGameComponent(gameComponent);

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

        WhomiApplication.get(this).setGameComponent(null);


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

    @Override
    public void quitGameWithError() {

        hideLoading();

        Toast.makeText(this, "There was problem with the server. Please try again later", Toast.LENGTH_SHORT).show();

        finish();

    }
}
