package com.danny.whomi.views;

import rx.Observable;

public interface QuestionBarView extends BaseView {

    void setClues(int integer);

    Observable<Void> shareClick();

    Observable<Void> clueClick();

    Observable<Void> menuClick();
}
