package com.whomi.presenters;

import com.whomi.services.PlayerService;
import com.whomi.model.objects.Player;
import com.whomi.navigator.GameNavigator;
import com.whomi.utils.RxUtils;
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

        Timber.d("startNextQuestion() called with: " + "");

        gameNavigator.showLoading();

        final Subscription subscription = playerService.getPlayer()
                .subscribe(this::displayQuestion, this::playerError);

        subscriptions.add(subscription);

    }

    private void playerError(Throwable throwable) {

        gameNavigator.quitGameWithError();

        Timber.e(throwable, "Loading player error");

    }

    private void displayQuestion(Player player) {

        Timber.d("displayQuestion() called with: " + "player = [" + player + "]");

        gameNavigator.hideLoading();

        gameNavigator.showQuestion(player);

    }

    public void skipQuestion() {

        Timber.d("skipQuestion() called with: " + "");

        playerService.markFinished();

        startNextQuestion();
    }

    public void finishQuestion() {

        Timber.d("finishQuestion() called with: " + "");

        playerService.markFinished();
    }

    public void startNext() {

        Timber.d("startNext() called with: " + "");

        startNextQuestion();
    }


    public void quitGame() {

        Timber.d("quitGame() called with: " + "");

        gameNavigator.quitGame();

    }

    public void finishGame() {

        Timber.d("finishGame() called with: " + "");

        playerService.close();
        RxUtils.safeUnsubscribe(subscriptions);

    }
}
