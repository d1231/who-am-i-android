package com.danny.projectt.model.database;

import android.content.Context;

import com.danny.projectt.MyApplication;
import com.danny.projectt.model.database.objects.RealmPlayer;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.model.objects.PlayerUtils;
import com.danny.projectt.utils.WorkerThread;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Func0;

public class RealmInteractor {

    private final Context context;

    private Realm realm;

    private WorkerThread workerThread;

    public RealmInteractor(Context context) {

        this.context = context;
    }

    public Observable<List<Player>> getItems() {

        return Observable.defer(() -> realm.where(RealmPlayer.class)
                                           .equalTo(RealmPlayer.GUESS_FINISH, false)
                                           .findAll()
                                           .asObservable()
                                           .filter(RealmResults::isLoaded)
                                           .filter(RealmResults::isValid))
                         .subscribeOn(HandlerScheduler.from(workerThread.handler))
                         .take(1)
                         .flatMap(Observable::from)
                         .map(PlayerUtils::fromRealm)
                         .collect((Func0<List<Player>>) Lists::newArrayList, List::add);
    }

    public void insert(List<Player> players) {

        workerThread.postTask(() -> {


            realm.executeTransactionAsync(realm1 -> {

                List<RealmPlayer> playerList = new ArrayList<>(players.size());
                for (Player player : players) {

                    RealmPlayer realmPlayer = new RealmPlayer(player);

                    playerList.add(realmPlayer);

                }

                realm1.insertOrUpdate(playerList);

            });

        });
    }

    public void markAsFinished(String id) {

        workerThread.postTask(() -> {

            realm.executeTransactionAsync(realm1 -> {

                final RealmPlayer realmPlayer = realm1.where(RealmPlayer.class)
                                                      .equalTo(RealmPlayer.ID, id)
                                                      .findFirst();

                realmPlayer.markAsFinished();

                realm1.insertOrUpdate(realmPlayer);

            });
        });

    }

    public void init() {


        RealmConfiguration configuration = new RealmConfiguration.Builder(context).build();

        workerThread = new WorkerThread("Realm-Thread");

        workerThread.start();

        workerThread.prepareHandler();

        workerThread.postTask(() -> realm = Realm.getInstance(configuration));
    }

    public void close() {

        workerThread.postTask(() -> {

            realm.close();
            realm = null;

        });

        workerThread.quitSafely();

        workerThread = null;
    }

}
