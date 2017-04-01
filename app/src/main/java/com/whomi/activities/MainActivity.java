package com.whomi.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.whomi.R;
import com.whomi.WhomiApplication;
import com.whomi.dagger.application.ApplicationComponent;
import com.whomi.dagger.main.DaggerMainComponent;
import com.whomi.dagger.main.MainModule;
import com.whomi.fragments.AndroidYesNoDialogBuilder;
import com.whomi.fragments.YesNoDialogBuilder;
import com.whomi.navigator.MenuNavigator;
import com.whomi.presenters.MainPresenter;
import com.whomi.views.MainView;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.main_play)
    View playView;

    @BindView(R.id.main_settings)
    View optionsView;

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_main);

        ButterKnife.bind(this);

        initDependencies();

        mainPresenter.attachView(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mainPresenter.detachView();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initDependencies() {

        final ApplicationComponent applicationComponent = WhomiApplication.get(this)
                .getApplicationComponent();

        MenuNavigator menuNavigator = new MenuNavigator() {
            @Override
            public void showGameScreen() {
                GameActivity.start(MainActivity.this);
            }

            @Override
            public void showOptionsScreen() {

                OptionsActivity.start(MainActivity.this);

            }
        };

        final DaggerMainComponent.Builder mainComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(menuNavigator))
                .applicationComponent(applicationComponent);

        mainComponent.build().inject(this);
    }

    @Override
    public Observable<Void> onOptionsClick() {
        return RxView.clicks(optionsView);
    }

    @Override
    public Observable<Void> onPlayClick() {

        return RxView.clicks(playView);
    }

    @Override
    public void showOnDailyClueBonusReceived(int dailyBonus) {

        Toast.makeText(MainActivity.this, getString(R.string.toast_daily_bonus, dailyBonus), Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    public YesNoDialogBuilder getDialogBuilder() {

        return AndroidYesNoDialogBuilder.create(this);
    }
}
