package com.danny.projectt.views;

import rx.Observable;

public interface MainView extends BaseView {

    Observable<Void> onPlayClick();

    void showOnDailyBonusReceived(int dailyBonus);

}
