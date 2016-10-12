package com.danny.projectt.views;

import rx.Observable;

public interface QuestionBarView extends BaseView {

    void setClues(int integer);

    void showTotalScore(int score);

    Observable<Void> clueClick();

    Observable<Void> menuClick();
}
