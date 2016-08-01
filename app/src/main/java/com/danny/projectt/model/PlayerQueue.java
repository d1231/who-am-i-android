package com.danny.projectt.model;

import com.danny.projectt.model.objects.Player;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Queue;

import rx.Observable;
import rx.subjects.PublishSubject;

public class PlayerQueue {

    private final int threshold;

    private Queue<Player> playerQueue;

    private PublishSubject<Void> itemAddedSubject = PublishSubject.create();

    public PlayerQueue(int threshold) {

        this.threshold = threshold;

        playerQueue = Lists.newLinkedList();
    }


    public Observable<Player> getItem() {

        final Observable<Player> pollItemObservable = Observable.defer(() -> Observable.just(pollItem()));

        if (playerQueue.isEmpty()) {
            return itemAdded().take(1).flatMap(r -> pollItemObservable);
        }

        return pollItemObservable;

    }

    private Player pollItem() {

        return playerQueue.poll();
    }

    public Observable<Void> itemAdded() {

        return itemAddedSubject;

    }

    public boolean belowThreshold() {

        return playerQueue.size() < threshold;
    }

    public void addItems(List<Player> playerList) {

        if (playerList.isEmpty()) {
            return;
        }

        playerQueue.addAll(playerList);
        itemAddedSubject.onNext(null);
    }

    public int size() {

        return playerQueue.size();
    }

    public void close() {

        playerQueue.clear();

    }
}
