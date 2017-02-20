package com.danny.projectt.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.danny.projectt.MyApplication;
import com.danny.projectt.R;
import com.danny.projectt.dagger.application.ApplicationComponent;
import com.danny.projectt.dagger.main.DaggerMainComponent;
import com.danny.projectt.dagger.main.MainModule;
import com.danny.projectt.fragments.AndroidYesNoDialogBuilder;
import com.danny.projectt.fragments.YesNoDialogBuilder;
import com.danny.projectt.navigator.MenuNavigator;
import com.danny.projectt.presenters.MainPresenter;
import com.danny.projectt.views.MainView;
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
