package com.danny.projectt;

import com.danny.projectt.services.PlayerService;
import com.danny.projectt.services.UserService;
import com.danny.projectt.model.network.BackendService;
import com.danny.projectt.model.objects.Player;
import com.google.common.collect.Lists;
import com.squareup.tape.InMemoryObjectQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static sharedTest.PlayerHelper.createPlayer;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {

    @Mock
    BackendService backendService;

    @Mock
    UserService userService;

    private PlayerService playerService;

    private InMemoryObjectQueue<Player> playerQueue;


    @Before
    public void setUp() throws Exception {

        playerQueue = new InMemoryObjectQueue<>();

        playerService = new PlayerService(backendService, userService, playerQueue);


    }

    @Test
    public void testNoSaved() throws Exception {

        final TestSubscriber<Player> subscriber = TestSubscriber.create();

        final Player player = createPlayer("David Garcia");
        final ArrayList<Player> result1 = Lists.newArrayList(player);

        when(backendService.getPlayer()).thenReturn(Observable.just(result1));

        playerService.getPlayer().subscribe(subscriber);

        verify(backendService, times(2)).getPlayer();

        subscriber.assertValue(player);
    }

    @Test
    public void testGetPlayer() throws Exception {

        final TestSubscriber<Player> subscriber = TestSubscriber.create();

        final Player player = createPlayer("David Garcia");
        final Player player2 = createPlayer("David Garcia2");
        final ArrayList<Player> result1 = Lists.newArrayList(player);

        playerQueue.add(player2);

        when(backendService.getPlayer()).thenReturn(Observable.just(result1));

        playerService.getPlayer().subscribe(subscriber);

        verify(backendService, times(1)).getPlayer();

        subscriber.assertValue(player2);
    }

}