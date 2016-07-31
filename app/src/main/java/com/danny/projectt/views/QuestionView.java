package com.danny.projectt.views;

import com.danny.projectt.Key;
import com.danny.projectt.model.objects.TeamHistory;

import java.util.List;

import rx.Observable;

public interface QuestionView extends BaseView {

    void setTeamHistory(List<TeamHistory> teamHistory);

    void setNumberOfClues(int integer);

    void updateQuestionScore(int score);

    void updateQuestionScore(int score, int change);

    void showTotalScore(int score);

    void setGuess(String guess);

    void correctGuess(Key key);

    void incorrectGuess(Key key);

    void showComplete();

    Observable<Key> guesses();

    Observable<Void> moveToNextClick();

    Observable<Void> clueClick();

    Observable<Void> skipClick();

    Observable<Void> menuClick();

}
