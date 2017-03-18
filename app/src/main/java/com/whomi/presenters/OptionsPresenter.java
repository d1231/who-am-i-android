package com.whomi.presenters;

import com.whomi.analytics_events.OptionsScreenEnterEvent;
import com.whomi.navigator.MenuNavigator;
import com.whomi.services.PlayerService;
import com.whomi.services.UserService;
import com.whomi.services.WhomiAnalyticsService;
import com.whomi.utils.RxUtils;
import com.whomi.views.OptionsView;

import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscription;
import timber.log.Timber;

public class OptionsPresenter extends BasePresenter<OptionsView> {

    private UserService userService;

    private PlayerService playerService;
    private MenuNavigator menuNavigator;

    private WhomiAnalyticsService whomiAnalyticsService;

    private OptionsView optionsView;

    private boolean queuedEmpty = false;

    @Inject
    public OptionsPresenter(UserService userService, PlayerService playerService, MenuNavigator menuNavigator, WhomiAnalyticsService whomiAnalyticsService) {
        this.userService = userService;
        this.playerService = playerService;
        this.menuNavigator = menuNavigator;
        this.whomiAnalyticsService = whomiAnalyticsService;
    }

    @Override
    public void attachView(OptionsView view) {

        optionsView = view;

        whomiAnalyticsService.logEvent(OptionsScreenEnterEvent.create());

        Subscription natSub = userService.getNationsStatus().subscribe(this::showNations, RxUtils::onError);

        Subscription nationStatusSub = optionsView.nationStatusChanged().subscribe(this::nationStatus, RxUtils::onError);

        optionsView.startGameButton().subscribe(res -> onStartGame(), RxUtils::onError);
        addSubscriptions(natSub, nationStatusSub);


    }

    private void onStartGame() {

        Timber.d("onStartGame() called with: " + "");

        menuNavigator.showGameScreen();

    }

    private void nationStatus(OptionsView.MultiSelectClick multiSelectClick) {

        Timber.d("nationStatus() called with: " + "multiSelectClick = [" + multiSelectClick + "]");


        if (!multiSelectClick.isChecked() && userService.getSelectedNations().size() == 1) {

            Timber.d("nationStatus(): Last Nation");

            optionsView.unableToUnchecked(multiSelectClick.getValue());
            return;
        }

        String nations = multiSelectClick.getValue();

        if (multiSelectClick.isChecked()) {
            userService.insertNewNation(nations);
        } else {
            userService.removeNation(nations);
        }

        if (!queuedEmpty) {
            playerService.clearQueue();
            queuedEmpty = true;
        }

    }

    private void showNations(Map<String, Boolean> nations) {

        Timber.d("showNations() called with: " + "nations = [" + nations + "]");

        optionsView.showNations(Collections.unmodifiableMap(nations));
    }
}
