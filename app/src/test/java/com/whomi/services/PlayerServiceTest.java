package com.whomi.services;

import com.google.common.collect.Lists;
import com.whomi.components.DaggerPlayerServiceComponent;
import com.whomi.model.network.BackendService;
import com.whomi.model.objects.Player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.observers.TestSubscriber;
import sharedTest.TestModule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static sharedTest.PlayerHelper.createPlayer;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {
//
//    @Rule
//    public final DaggerMockRule<PlayerServiceComponent> rule = new DaggerMockRule<>(PlayerServiceComponent.class, new TestModule())
//            .set(component -> component.inject(PlayerServiceTest.this));

    @Inject
    BackendService backendService;

    @Inject
    PlayerService playerService;

    @Before
    public void setUp() throws Exception {

        DaggerPlayerServiceComponent.builder().testModule(new TestModule()).build().inject(this);

    }

    @Test
    public void testPrefetch() throws Exception {

        final TestSubscriber<Player> subscriber = TestSubscriber.create();

        final Player player = createPlayer("David Garcia");
        final ArrayList<Player> result1 = Lists.newArrayList(player);

        when(backendService.getPlayer(any())).thenReturn(Observable.just(result1));

        playerService.getPlayer().subscribe(subscriber);

        verify(backendService, times(2)).getPlayer(any());

        subscriber.assertValue(player);
    }

    @Test
    public void testGetPlayer() throws Exception {

        final TestSubscriber<Player> subscriber = TestSubscriber.create();

        ArrayList<ArrayList<Player>> list = Lists.newArrayList();

        final Player player = createPlayer("David Garcia");
        final ArrayList<Player> result1 = Lists.newArrayList(player);
        list.add(result1);
        for (int i = 0; i < PlayerService.THRESHOLD; i++) {

            final Player player2 = createPlayer("David Garcia2");
            ArrayList<Player> list2 = Lists.newArrayList();
            list2.add(player2);

            list.add(list2);
        }

        when(backendService.getPlayer(any())).thenReturn(Observable.from(list));

        playerService.getPlayer().subscribe(subscriber);

        verify(backendService, atLeastOnce()).getPlayer(any());

        subscriber.assertValue(player);

    }

}