package com.whomi.presenters;

import com.whomi.navigator.MenuNavigator;
import com.whomi.services.ClueService;
import com.whomi.utils.DateUtils;
import com.whomi.views.MainView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by danny on 11-Mar-17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @InjectMocks
    MainPresenter mainPresenter;

    @Mock
    MenuNavigator menuNavigator;

    @Mock
    ClueService clueService;

    @Mock
    MainView mainView;

    @Before
    public void setUp() throws Exception {

        when(mainView.onOptionsClick()).thenReturn(Observable.never());
        when(mainView.onPlayClick()).thenReturn(Observable.never());

        when(clueService.lastClueBonusTimestamp()).thenReturn((long) 0);

    }

    @After
    public void tearDown() throws Exception {

        mainPresenter.detachView();

    }

    @Test
    public void testPlayButton() throws Exception {

        when(mainView.onPlayClick()).thenReturn(Observable.defer(() -> Observable.just(null)));

        mainPresenter.attachView(mainView);

        verify(menuNavigator, times(1)).showGameScreen();

    }

    @Test
    public void testOptionsButton() throws Exception {


        when(mainView.onOptionsClick()).thenReturn(Observable.defer(() -> Observable.just(null)));

        mainPresenter.attachView(mainView);

        verify(menuNavigator, times(1)).showOptionsScreen();

    }

    @Test
    public void testDailyClue() throws Exception {

        mainPresenter.attachView(mainView);

        verify(mainView, times(1)).showOnDailyClueBonusReceived(MainPresenter.DAILY_BONUS);
        verify(clueService, times(1)).addClues(MainPresenter.DAILY_BONUS);

    }


    @Test
    public void testDailyClueNotGiven() throws Exception {

        Date date = DateUtils.getCalendarDate(System.currentTimeMillis());

        when(clueService.lastClueBonusTimestamp()).thenReturn(date.getTime());

        mainPresenter.attachView(mainView);

        verify(mainView, times(0)).showOnDailyClueBonusReceived(any());
        verify(clueService, times(0)).addClues(any());

    }
}
