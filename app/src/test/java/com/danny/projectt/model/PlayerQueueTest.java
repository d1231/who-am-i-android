package com.danny.projectt.model;

import com.danny.projectt.model.objects.Player;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import rx.Observable;
import rx.observers.TestSubscriber;
import sharedTest.PlayerHelper;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlayerQueueTest {

    private PlayerQueue playerQueue;

    @Before
    public void setUp() throws Exception {

        playerQueue = new PlayerQueue(2);

    }

    @Test
    public void testGetItemAlreadyIn() throws Exception {

        final ArrayList<Player> playerList = Lists.newArrayList(PlayerHelper.createPlayer(1));
        playerQueue.addItems(playerList);

        testReceivedPlayer(playerList.get(0));

    }

    private void testReceivedPlayer(Player player) {

        final TestSubscriber<Object> testSubscriber = TestSubscriber.create();
        playerQueue.getItem().subscribe(testSubscriber);
        testSubscriber.assertValue(player);
        testSubscriber.assertCompleted();
    }

    @Test
    public void testGetItemMultiple() throws Exception {

        final ArrayList<Player> playerList = Lists.newArrayList(PlayerHelper.createPlayer(1), PlayerHelper
                .createPlayer(2), PlayerHelper.createPlayer(3));
        playerQueue.addItems(playerList);

        testReceivedPlayer(playerList.get(0));
        testReceivedPlayer(playerList.get(1));
        testReceivedPlayer(playerList.get(2));

    }

    @Test
    public void testGetItemAddWhileIn() throws Exception {

        final ArrayList<Player> playerList = Lists.newArrayList(PlayerHelper.createPlayer(1));

        final TestSubscriber<Object> testSubscriber = TestSubscriber.create();
        playerQueue.getItem().subscribe(testSubscriber);

        testSubscriber.assertNoValues();

        playerQueue.addItems(playerList);

        testSubscriber.assertValue(playerList.get(0));

    }

    @Test
    public void testItemAdded() throws Exception {

        final Observable<Void> itemAdded = playerQueue.itemAdded();

        final TestSubscriber<Void> subscriber = TestSubscriber.create();
        itemAdded.subscribe(subscriber);

        playerQueue.addItems(Lists.newArrayList());

        subscriber.assertNoValues();

        playerQueue.addItems(Lists.newArrayList(PlayerHelper.createPlayer(50)));

        subscriber.assertValue(null);
    }

    @Test
    public void testBelowThreshold() throws Exception {

        assertThat(playerQueue.belowThreshold(), is(false));
    }

}