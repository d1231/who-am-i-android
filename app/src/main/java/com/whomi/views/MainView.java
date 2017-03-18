package com.whomi.views;

import rx.Observable;

public interface MainView extends BaseView {

    Observable<Void> onOptionsClick();

    Observable<Void> onPlayClick();

    void showOnDailyClueBonusReceived(int dailyBonus);

}
