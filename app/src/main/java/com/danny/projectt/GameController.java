package com.danny.projectt;

import com.danny.projectt.services.PlayerService;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.navigator.GameNavigator;
import com.danny.projectt.utils.RxUtils;
import com.google.common.collect.Lists;

import java.util.List;

import rx.Subscription;
import timber.log.Timber;

public class GameController {

    private final GameNavigator gameNavigator;

    private final PlayerService playerService;

    private final List<Subscription> subscriptions = Lists.newArrayList();

    public GameController(PlayerService playerService, GameNavigator gameNavigator) {

        this.playerService = playerService;
        this.gameNavigator = gameNavigator;
    }

    public void startGame() {

        startNextQuestion();

    }

    private void startNextQuestion() {

        gameNavigator.showLoading();

        final Subscription subscription = playerService.getPlayer()
                                                       .subscribe(this::displayQuestion, this::playerError);

        subscriptions.add(subscription);

    }

    private void playerError(Throwable throwable) {

        // todo network error
        //


        Timber.e(throwable, "Loading player error");

    }

    private void displayQuestion(Player player) {

        gameNavigator.hideLoading();

        gameNavigator.showQuestion(player);

    }

    public void skipQuestion() {

        playerService.markFinished();


        startNextQuestion();
    }

    public void finishQuestion() {

        playerService.markFinished();
    }

    public void startNext() {

        startNextQuestion();
    }


    public void quitGame() {

        gameNavigator.quitGame();

    }

    public void finishGame() {

        playerService.close();
        RxUtils.safeUnsubscribe(subscriptions);

    }
}
