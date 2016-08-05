package com.danny.projectt.model;

import com.danny.projectt.model.network.BackendService;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.utils.RxUtils;
import com.google.common.collect.Lists;
import com.squareup.tape.ObjectQueue;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

public class PlayerRepository {

    private static final int THRESHOLD = 5;

    private final BackendService backendService;

    private final ObjectQueue<Player> playerQueue;

    private List<Subscription> subscriptions;

    @Inject
    public PlayerRepository(BackendService backendService, ObjectQueue<Player> playerQueue) {

        this.backendService = backendService;
        this.playerQueue = playerQueue;

        subscriptions = Lists.newArrayList();


    }

    public Observable<Player> getPlayer() {

        BehaviorSubject<Player> playerPublishSubject = BehaviorSubject.create();

        final Player topPlayer = playerQueue.peek();

        if (topPlayer == null) {

            ObjectQueue.Listener<Player> listener = new PlayerQueueListener(playerPublishSubject);
            playerQueue.setListener(listener);

            final Subscription subscription = backendService.getPlayer()
                                                            .subscribe(players -> {

                                                                for (Player player : players) {
                                                                    playerQueue.add(player);
                                                                }
                                                            }, (e) -> {
                                                                playerQueue.setListener(null);
                                                                playerPublishSubject.onError(e);
                                                            });

            subscriptions.add(subscription);

        } else {
            playerPublishSubject.onNext(topPlayer);
        }

        return playerPublishSubject.doOnNext(player -> fillUpQueue());
    }

    private void fillUpQueue() {

        if (playerQueue.size() >= THRESHOLD) {
            return;
        }

        Timber.d("Filling queue, current queue size is %d", playerQueue.size());

        final Subscription subscription = backendService.getPlayer()
                                                        .subscribe(players -> {

                                                            for (Player player : players) {
                                                                playerQueue.add(player);
                                                            }

                                                        }, err -> {


                                                        });

        subscriptions.add(subscription);

    }

    public void markFinished() {

        playerQueue.remove();
    }

    public void close() {

        RxUtils.safeUnsubscribe(subscriptions);

    }

    private static class PlayerQueueListener implements ObjectQueue.Listener<Player> {

        private final BehaviorSubject<Player> playerPublishSubject;

        public PlayerQueueListener(BehaviorSubject<Player> playerPublishSubject) {

            this.playerPublishSubject = playerPublishSubject;
        }

        @Override
        public void onAdd(ObjectQueue<Player> queue, Player entry) {

            playerPublishSubject.onNext(entry);
            queue.setListener(null);

        }

        @Override
        public void onRemove(ObjectQueue<Player> queue) {

        }
    }
}
