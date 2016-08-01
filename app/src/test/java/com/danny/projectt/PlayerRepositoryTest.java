package com.danny.projectt;

import com.danny.projectt.model.PlayerRepository;
import com.danny.projectt.model.database.RealmInteractor;
import com.danny.projectt.model.network.BackendService;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static sharedTest.PlayerHelper.createPlayer;

@RunWith(MockitoJUnitRunner.class)
public class PlayerRepositoryTest {

    @Mock
    BackendService backendService;

    @Mock
    RealmInteractor realmInteractor;

    PlayerRepository playerRepository;

    @Before
    public void setUp() throws Exception {

        when(realmInteractor.getItems()).thenReturn(Observable.just(Lists.newArrayList()));

        playerRepository = new PlayerRepository(backendService, realmInteractor);


    }

    @Test
    public void testNoSaved() throws Exception {

        final TestSubscriber<Player> subscriber = TestSubscriber.create();

        final Player player = createPlayer("David Garcia");
        final ArrayList<Player> result1 = Lists.newArrayList(player);

        when(backendService.getPlayer()).thenReturn(Observable.just(result1));

        playerRepository.start();

        playerRepository.getPlayer().subscribe(subscriber);

        verify(backendService, times(2)).getPlayer();

        subscriber.assertValue(player);
    }

    @Test
    public void testGetPlayer() throws Exception {

        final TestSubscriber<Player> subscriber = TestSubscriber.create();

        final Player player = createPlayer("David Garcia");
        final ArrayList<Player> result1 = Lists.newArrayList(player);
        final ArrayList<Player> result2 = Lists.newArrayList(player, player);

        when(backendService.getPlayer()).thenReturn(Observable.just(result1));

        when(realmInteractor.getItems()).thenReturn(Observable.just(result2));

        playerRepository.start();

        playerRepository.getPlayer().subscribe(subscriber);

        verify(backendService, times(1)).getPlayer();

        subscriber.assertValue(player);
    }

}