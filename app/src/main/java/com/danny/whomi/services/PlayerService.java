package com.danny.whomi.services;

import com.danny.whomi.model.network.BackendService;
import com.danny.whomi.model.objects.GameOptions;
import com.danny.whomi.model.objects.Player;
import com.danny.whomi.utils.RxUtils;
import com.google.common.collect.Lists;
import com.squareup.tape.ObjectQueue;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

public class PlayerService {

    public static final int THRESHOLD = 5;

    private final BackendService backendService;

    private final UserService userService;

    private final ObjectQueue<Player> playerQueue;

    private List<Subscription> subscriptions;

    @Inject
    public PlayerService(BackendService backendService, UserService userService, ObjectQueue<Player> playerQueue) {

        this.backendService = backendService;
        this.userService = userService;
        this.playerQueue = playerQueue;

        subscriptions = Lists.newArrayList();


    }

    public Observable<Player> getPlayer() {

        BehaviorSubject<Player> playerPublishSubject = BehaviorSubject.create();

        final Player topPlayer = playerQueue.peek();

        if (topPlayer == null) {

            // set up listener to publish result to subscribers
            ObjectQueue.Listener<Player> listener = new PlayerQueueListener(playerPublishSubject);
            playerQueue.setListener(listener);

            final GameOptions gameOptions = userService.getGameOptions();

            final Subscription subscription = backendService.getPlayer(gameOptions.toQueryMap())
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

        final GameOptions gameOptions = userService.getGameOptions();

        final Subscription subscription = backendService.getPlayer(gameOptions.toQueryMap())
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

        PlayerQueueListener(BehaviorSubject<Player> playerPublishSubject) {

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
