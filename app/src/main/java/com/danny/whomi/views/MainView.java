package com.danny.whomi.views;

import rx.Observable;

public interface MainView extends BaseView {

    Observable<Void> onPlayClick();

    void showOnDailyClueBonusReceived(int dailyBonus);

}
