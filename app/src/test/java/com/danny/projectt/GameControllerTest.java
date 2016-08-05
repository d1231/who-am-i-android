package com.danny.projectt;

import com.danny.projectt.model.PlayerRepository;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.navigator.GameNavigator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static sharedTest.PlayerHelper.getDummyPlayer;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

    @Mock
    PlayerRepository playerRepository;

    @Mock
    GameNavigator gameNavigator;

    @InjectMocks
    GameController gameController;

    @Before
    public void setUp() throws Exception {

        when(playerRepository.getPlayer()).thenReturn(Observable.never());

    }

    @Test
    public void testStartGame() throws Exception {

        Player player = getDummyPlayer();
        when(playerRepository.getPlayer()).thenReturn(Observable.defer(() -> Observable.just(player)));

        gameController.startGame();

        verify(gameNavigator, times(1)).showQuestion(player);

    }

    @Test
    public void testFinishQuestion() throws Exception {

        gameController.finishQuestion();

        verify(playerRepository, times(1)).getPlayer();
    }

    @Test
    public void testQuitGame() throws Exception {

        gameController.quitGame();

        verify(gameNavigator, times(1)).quitGame();
    }
}