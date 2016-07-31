package com.danny.projectt;

import com.danny.projectt.model.network.BackendService;
import com.danny.projectt.model.PlayerRepository;
import com.danny.projectt.model.objects.Player;
import com.google.common.collect.Lists;

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

@RunWith(MockitoJUnitRunner.class)
public class PlayerRepositoryTest {

    @Mock
    BackendService backendService;

    private PlayerRepository playerRepository;

    @Before
    public void setUp() throws Exception {

        playerRepository = new PlayerRepository(backendService);

    }

    @Test
    public void testGetPlayer() throws Exception {

        final TestSubscriber<Player> subscriber = TestSubscriber.create();

        final Player player = createPlayer("David Garcia");
        final ArrayList<Player> result1 = Lists.newArrayList(player);

        when(backendService.getPlayer()).thenReturn(Observable.just(result1));

        playerRepository.getPlayer().subscribe(subscriber);

        verify(backendService, times(2)).getPlayer();

        subscriber.assertValue(player);
    }

    public Player createPlayer(String name) {

        return Player.create(name, "Goalkeeper", "Spain", "10.8.94", Lists.newArrayList());
    }

    @Test
    public void testPrefetchPlayer() throws Exception {


        final TestSubscriber<Player> subscriber = TestSubscriber.create();
        final TestSubscriber<Player> subscriber2 = TestSubscriber.create();

        final Player player = createPlayer("David Garcia");
        final Player player1 = createPlayer("David Garcia The Second");
        final ArrayList<Player> result1 = Lists.newArrayList(player);
        final ArrayList<Player> result2 = Lists.newArrayList(player1);

        when(backendService.getPlayer()).thenReturn(Observable.just(result1), Observable.just(result2));

        playerRepository.prefetchPlayer();

        verify(backendService, times(1)).getPlayer();

        playerRepository.getPlayer().subscribe(subscriber);
        subscriber.assertValue(player);
        verify(backendService, times(2)).getPlayer();

        playerRepository.getPlayer().subscribe(subscriber2);
        subscriber2.assertValue(player1);
        verify(backendService, times(3)).getPlayer();


    }
}