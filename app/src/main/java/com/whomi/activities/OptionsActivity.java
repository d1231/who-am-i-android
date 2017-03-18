package com.whomi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.whomi.WhomiApplication;
import com.whomi.R;
import com.whomi.adapters.NationAdapter;
import com.whomi.dagger.application.ApplicationComponent;
import com.whomi.dagger.main.DaggerMainComponent;
import com.whomi.dagger.main.MainModule;
import com.whomi.fragments.AndroidYesNoDialogBuilder;
import com.whomi.fragments.YesNoDialogBuilder;
import com.whomi.navigator.MenuNavigator;
import com.whomi.presenters.OptionsPresenter;
import com.whomi.views.OptionsView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public class OptionsActivity extends AppCompatActivity implements OptionsView, NationAdapter.NationListener, MenuNavigator {

    private NationAdapter adapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, OptionsActivity.class);
        context.startActivity(starter);
    }

    @BindView(R.id.options_nations)
    RecyclerView recyclerView;

    @BindView(R.id.options_startgame)
    View startGame;

    @Inject
    OptionsPresenter optionsPresenter;

    private PublishSubject<MultiSelectClick> nationSelectSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_options);

        ButterKnife.bind(this);

        initDependencies();

        optionsPresenter.attachView(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        optionsPresenter.detachView();

    }

    private void initDependencies() {

        final ApplicationComponent applicationComponent = WhomiApplication.get(this)
                .getApplicationComponent();

        final DaggerMainComponent.Builder mainComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .applicationComponent(applicationComponent);

        mainComponent.build().inject(this);

    }

    @Override
    public Observable<Void> startGameButton() {
        return RxView.clicks(startGame);
    }

    @Override
    public Observable<MultiSelectClick> nationStatusChanged() {
        return nationSelectSubject;
    }

    @Override
    public void showNations(Map<String, Boolean> nations) {
        runOnUiThread(() -> setNationList(nations));
    }

    @Override
    public void unableToUnchecked(String value) {

        runOnUiThread(() -> {

            Toast.makeText(this, "At least 1 nations must be selected", Toast.LENGTH_SHORT).show();

            adapter.setChecked(value);

        });

    }

    @UiThread
    private void setNationList(Map<String, Boolean> nations) {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NationAdapter(this, nations, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public YesNoDialogBuilder getDialogBuilder() {

        return AndroidYesNoDialogBuilder.create(this);
    }

    @Override
    public void checked(String nation, boolean isChecked) {

        nationSelectSubject.onNext(new MultiSelectClick(nation, isChecked));

    }

    @Override
    public void showGameScreen() {
        GameActivity.start(OptionsActivity.this);
    }

    @Override
    public void showOptionsScreen() {
        throw new IllegalStateException("Illegal state");
    }
}
