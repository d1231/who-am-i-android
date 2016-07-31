package com.danny.projectt.model;

import com.danny.projectt.model.network.BackendService;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.utils.RxUtils;
import com.google.common.collect.Lists;

import java.util.Queue;

import javax.inject.Inject;

import rx.Observable;

public class PlayerRepository {

    private final BackendService backendService;

    private Queue<Player> playerList = Lists.newLinkedList();

    @Inject
    public PlayerRepository(BackendService backendService) {

        this.backendService = backendService;
    }

    public Observable<Player> getPlayer() {

        return Observable.defer(() -> Observable.just(playerList.poll()))
                         .flatMap(player -> {
                             if (player == null) {
                                 // todo
                                 return backendService.getPlayer()
                                                      .map(playerList -> playerList.get(0))
                                                      .retry(2);
                             } else {
                                 return Observable.just(player);
                             }
                         })
                         .doOnNext(res -> prefetchPlayer());
    }

    public void prefetchPlayer() {

        backendService.getPlayer()
                      .flatMap(Observable::from)
                      .subscribe(player -> playerList.add(player), RxUtils::onError);
    }

}
