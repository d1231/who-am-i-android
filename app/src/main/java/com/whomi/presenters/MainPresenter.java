package com.whomi.presenters;

import com.whomi.analytics_events.DailyClueEvent;
import com.whomi.analytics_events.MainScreenEnterEvent;
import com.whomi.navigator.MenuNavigator;
import com.whomi.services.ClueService;
import com.whomi.services.WhomiAnalyticsService;
import com.whomi.utils.DateUtils;
import com.whomi.utils.RxUtils;
import com.whomi.views.MainView;

import java.util.Date;

import javax.inject.Inject;

import rx.Subscription;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainView> {

    public static final int DAILY_BONUS = 10;

    private final ClueService clueService;

    private final MenuNavigator menuNavigator;
    private WhomiAnalyticsService whomiAnalyticsService;

    private MainView view;

    @Inject
    MainPresenter(ClueService clueService, MenuNavigator menuNavigator, WhomiAnalyticsService whomiAnalyticsService) {

        this.clueService = clueService;
        this.menuNavigator = menuNavigator;
        this.whomiAnalyticsService = whomiAnalyticsService;

    }

    @Override
    public void attachView(MainView view) {

        this.view = view;

        whomiAnalyticsService.logEvent(MainScreenEnterEvent.create());

        final Subscription playSub = view.onPlayClick().subscribe(res -> goToGameScreen(), RxUtils::onError);

        Subscription optionsSub = view.onOptionsClick().subscribe(res -> goToOptionsScreen(), RxUtils::onError);

        addSubscriptions(playSub, optionsSub);

        clueBonus();
    }

    private void goToGameScreen() {

        Timber.d("goToGameScreen() called with: " + "");

        menuNavigator.showGameScreen();
    }

    private void goToOptionsScreen() {

        Timber.d("goToOptionsScreen() called with: " + "");

        menuNavigator.showOptionsScreen();

    }

    private void clueBonus() {

        final Date savedCalendar = DateUtils.getCalendarDate(clueService.lastClueBonusTimestamp());
        final Date currentCalendar = DateUtils.getCalendarDate(System.currentTimeMillis());

        Timber.d("clueBonus() called with: savedCalendar=[" + savedCalendar + "]");

        if (currentCalendar.after(savedCalendar)) {

            Timber.d("clueBonus(): clue given");

            whomiAnalyticsService.logEvent(DailyClueEvent.create(savedCalendar.getTime()));

            clueService.addClues(DAILY_BONUS);
            view.showOnDailyClueBonusReceived(DAILY_BONUS);

        }
    }

    @Override
    public void detachView() {

        super.detachView();
        view = null;

    }
}
