package com.danny.projectt.presenters;

import com.danny.projectt.GameController;
import com.danny.projectt.Key;
import com.danny.projectt.model.ClueRepository;
import com.danny.projectt.model.ScoreRepository;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.views.QuestionView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import sharedTest.PlayerHelper;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionPresenterTest {

    @Mock
    GameController gameController;

    @Mock
    QuestionView questionView;

    @Mock
    ScoreRepository scoreRepository;

    @Mock
    ClueRepository clueRepo;

    QuestionPresenter questionPresenter;

    Player dummyPlayer = PlayerHelper.getDummyPlayer();

    @Before
    public void setUp() throws Exception {

        questionPresenter = new QuestionPresenter(gameController, scoreRepository, clueRepo, dummyPlayer);

        when(questionView.guesses()).thenReturn(Observable.never());
        when(questionView.clueClick()).thenReturn(Observable.never());
        when(questionView.moveToNextClick()).thenReturn(Observable.never());
        when(questionView.skipClick()).thenReturn(Observable.never());
        when(questionView.menuClick()).thenReturn(Observable.never());

        when(scoreRepository.getTotalScoreObservable()).thenReturn(Observable.never());

        when(clueRepo.getCluesObservable()).thenReturn(Observable.never());
        when(clueRepo.getClues()).thenReturn(0);
    }

    @Test
    public void testBindTeamHistory() throws Exception {

        questionPresenter.attachView(questionView);

        verify(questionView, times(1)).setTeamHistory(dummyPlayer.teamHistory());
    }

    @Test
    public void testCorrectGuess() throws Exception {

        when(questionView.guesses()).thenReturn(Observable.defer(() -> Observable.just(Key.E)));

        questionPresenter.attachView(questionView);

        verify(questionView, times(1)).correctGuess(Key.E);
    }

    @Test
    public void testIncorrectGuess() throws Exception {

        when(questionView.guesses()).thenReturn(Observable.defer(() -> Observable.just(Key.Q)));

        questionPresenter.attachView(questionView);

        verify(questionView, times(1)).incorrectGuess(Key.Q);
    }

    @Test
    public void testGuessFinish() throws Exception {

        Key[] arr = {Key.N, Key.E, Key.I, Key.L, Key.M, Key.A, Key.D, Key.S, Key.O};

        when(questionView.guesses()).thenReturn(Observable.defer(() -> Observable.from(arr)));

        questionPresenter.attachView(questionView);

        verify(questionView, times(arr.length)).correctGuess(any());

        verify(questionView, times(1)).showComplete();

        verify(scoreRepository, times(1)).setSequence(arr.length);

        verify(scoreRepository, times(1)).addQuestionScore(anyInt());

    }

    @Test
    public void testClue() throws Exception {

        when(questionView.clueClick()).thenReturn(Observable.defer(() -> Observable.just(null)));

        when(clueRepo.getClues()).thenReturn(1);

        questionPresenter.attachView(questionView);

        verify(questionView, times(2)).setGuess(anyString());

        verify(clueRepo, times(1)).clueUsed();

    }

}