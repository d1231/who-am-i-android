package com.danny.projectt.presenters;

import com.danny.projectt.model.ClueService;
import com.danny.projectt.navigator.MenuNavigator;
import com.danny.projectt.utils.DateUtils;
import com.danny.projectt.utils.RxUtils;
import com.danny.projectt.views.MainView;

import java.util.Date;

import javax.inject.Inject;

import rx.Subscription;

public class MainPresenter extends BasePresenter<MainView> {

    private static final int DAILY_BONUS = 10;

    private final ClueService clueService;

    private final MenuNavigator menuNavigator;

    private MainView view;

    @Inject
    MainPresenter(ClueService clueService, MenuNavigator menuNavigator) {

        this.clueService = clueService;

        this.menuNavigator = menuNavigator;
    }

    @Override
    public void attachView(MainView view) {

        this.view = view;

        final Subscription playSub = view.onPlayClick().subscribe(res -> {
            menuNavigator.showGameScreen();
        }, RxUtils::onError);

        addSubscriptions(playSub);

        clueBonus();
    }

    private void clueBonus() {

        final Date savedCalendar = DateUtils.getCalendarDate(clueService.lastClueBonusTimestamp());
        final Date currentCalendar = DateUtils.getCalendarDate(System.currentTimeMillis());

        if (currentCalendar.after(savedCalendar)) {

            clueService.addClues(DAILY_BONUS);
            clueService.setBonusTimeStamp(currentCalendar.getTime());

            view.showOnDailyClueBonusReceived(DAILY_BONUS);
        }
    }

    @Override
    public void detachView() {

        super.detachView();
        view = null;

    }
}
