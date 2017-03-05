package com.danny.whomi.views;

import com.danny.whomi.Key;
import com.danny.whomi.model.objects.TeamHistory;

import java.util.List;

import rx.Observable;

public interface QuestionView extends BaseView {

    QuestionBarView getQuestionBarView();

    void setTeamHistory(List<TeamHistory> teamHistory);

    void setGuess(String guess);

    void correctGuess(Key key);

    void incorrectGuess(Key key);

    void showComplete();

    Observable<Key> guesses();

    Observable<Void> moveToNextClick();

    Observable<Void> skipClick();

}
