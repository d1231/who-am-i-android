package com.danny.projectt.model;

import com.danny.projectt.model.database.RealmInteractor;
import com.danny.projectt.model.network.BackendService;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.utils.RxUtils;
import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

public class PlayerRepository {

    private final BackendService backendService;

    private final RealmInteractor realmInteractor;

    private PlayerQueue playerQueue;

    private List<Subscription> subscriptions;

    @Inject
    public PlayerRepository(BackendService backendService, RealmInteractor realmInteractor) {

        this.backendService = backendService;
        this.realmInteractor = realmInteractor;
        this.playerQueue = new PlayerQueue(2);

        subscriptions = Lists.newArrayList();


    }

    public void start() {

        Timber.d("Initiating PlayerRepository");

        realmInteractor.init();

        final Subscription subscription = realmInteractor.getItems()
                                                         .doOnCompleted(() -> {
                                                             if (playerQueue.belowThreshold()) {
                                                                 fillUpQueue();
                                                             }
                                                         })
                                                         .subscribe(r -> {
                                                             Timber.d("List: %s", r);
                                                             playerQueue.addItems(r);
                                                         }, RxUtils::onError);

        subscriptions.add(subscription);

    }

    private void fillUpQueue() {

        Timber.d("Filling queue, current queue size is %d", playerQueue.size());

        final Subscription subscription = backendService.getPlayer()
                                                        .doOnNext(realmInteractor::insert)
                                                        .subscribe(players -> {
                                                            Timber.d("List: %s", players);
                                                            playerQueue.addItems(players);
                                                        }, RxUtils::onError);

        subscriptions.add(subscription);

    }

    public Observable<Player> getPlayer() {

        return playerQueue.getItem().doOnNext(player -> {
            if (playerQueue.belowThreshold()) {
                fillUpQueue();
            }
        });

    }

    public void markFinished(Player player) {

        realmInteractor.markAsFinished(player.id());
    }


    public void close() {

        RxUtils.safeUnsubscribe(subscriptions);

        realmInteractor.close();

        playerQueue.close();
    }
}
