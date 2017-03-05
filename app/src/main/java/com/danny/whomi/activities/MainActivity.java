package com.danny.whomi.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.danny.whomi.MyApplication;
import com.danny.whomi.R;
import com.danny.whomi.dagger.application.ApplicationComponent;
import com.danny.whomi.dagger.main.DaggerMainComponent;
import com.danny.whomi.dagger.main.MainModule;
import com.danny.whomi.fragments.AndroidYesNoDialogBuilder;
import com.danny.whomi.fragments.YesNoDialogBuilder;
import com.danny.whomi.navigator.MenuNavigator;
import com.danny.whomi.presenters.MainPresenter;
import com.danny.whomi.views.MainView;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.main_play)
    View playView;

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

    private void initDependencies() {

        final ApplicationComponent applicationComponent = MyApplication.get(this)
                                                                       .getApplicationComponent();

        //noinspection Convert2Lambda
        MenuNavigator menuNavigator = new MenuNavigator() {

            @Override
            public void showGameScreen() {

                GameActivity.start(MainActivity.this);
            }

        };

        final DaggerMainComponent.Builder mainComponent = DaggerMainComponent.builder()
                                                                             .mainModule(new MainModule(menuNavigator))
                                                                             .applicationComponent(applicationComponent);

        mainComponent.build().inject(this);
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
