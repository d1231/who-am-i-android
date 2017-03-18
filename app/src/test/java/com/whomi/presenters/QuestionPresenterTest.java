package com.whomi.presenters;

import com.whomi.Key;
import com.whomi.model.objects.Player;
import com.whomi.services.ClueService;
import com.whomi.services.ShareService;
import com.whomi.views.QuestionBarView;
import com.whomi.views.QuestionView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;
import sharedTest.PlayerHelper;

import static org.mockito.Matchers.any;
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
    ClueService clueRepo;

    @Mock
    ShareService shareService;

    @Mock
    QuestionBarView questionBarView;

    @InjectMocks
    private QuestionPresenter questionPresenter;

    private Player player;

    @Before
    public void setUp() throws Exception {

        player = PlayerHelper.getDummyPlayer();

        when(questionView.guesses()).thenReturn(Observable.never());
        when(questionView.moveToNextClick()).thenReturn(Observable.never());
        when(questionView.skipClick()).thenReturn(Observable.never());

        when(questionView.getQuestionBarView()).thenReturn(questionBarView);

        when(questionBarView.clueClick()).thenReturn(Observable.never());
        when(questionBarView.menuClick()).thenReturn(Observable.never());

        when(clueRepo.getCluesObservable()).thenReturn(Observable.never());
        when(clueRepo.getClues()).thenReturn(0);
    }

    @After
    public void cleanup() throws Exception {

        questionPresenter.detachView();

    }

    @Test
    public void testBindTeamHistory() throws Exception {


        verify(questionView, times(1)).setTeamHistory(player.teamHistory());
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

    }

    @Test
    public void testClue() throws Exception {

        when(questionBarView.clueClick()).thenReturn(Observable.defer(() -> Observable.just(null)));

        when(clueRepo.getClues()).thenReturn(1);

        questionPresenter.attachView(questionView);

        verify(questionView, times(2)).setGuess(anyString());

        verify(clueRepo, times(1)).clueUsed();

    }

}