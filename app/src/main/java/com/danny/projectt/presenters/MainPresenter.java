package com.danny.projectt.presenters;

import com.danny.projectt.navigator.MenuNavigator;
import com.danny.projectt.model.ClueRepository;
import com.danny.projectt.utils.DateUtils;
import com.danny.projectt.utils.RxUtils;
import com.danny.projectt.views.MainView;

import java.util.Date;

import javax.inject.Inject;

import rx.Subscription;

public class MainPresenter extends BasePresenter<MainView> {

    private static final int DAILY_BONUS = 10;

    private final ClueRepository clueRepository;

    private final MenuNavigator menuNavigator;

    private MainView view;

    @Inject
    public MainPresenter(ClueRepository clueRepository, MenuNavigator menuNavigator) {

        this.clueRepository = clueRepository;

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

        final Date savedCalendar = DateUtils.getCalendarDate(clueRepository.lastClueBonusTimestamp());
        final Date currentCalendar = DateUtils.getCalendarDate(System.currentTimeMillis());

        if (currentCalendar.after(savedCalendar)) {

            clueRepository.addClues(DAILY_BONUS);
            clueRepository.setBonusTimeStamp(currentCalendar.getTime());

            view.showDailyBonus(DAILY_BONUS);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        view = null;

    }
}
