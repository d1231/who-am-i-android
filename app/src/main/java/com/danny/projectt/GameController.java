package com.danny.projectt;

import com.danny.projectt.model.PlayerRepository;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.navigator.GameNavigator;
import com.danny.projectt.utils.RxUtils;
import com.google.common.collect.Lists;

import java.util.List;

import rx.Subscription;

public class GameController {

    private final GameNavigator gameNavigator;

    private final PlayerRepository playerRepository;

    private final List<Subscription> subscriptions = Lists.newArrayList();

    public GameController(PlayerRepository playerRepository, GameNavigator gameNavigator) {

        this.playerRepository = playerRepository;
        this.gameNavigator = gameNavigator;
    }

    public void startGame() {

        playerRepository.start();

        startNextQuestion();

    }

    private void startNextQuestion() {

        final Subscription subscription = playerRepository.getPlayer()
                                                          .subscribe(this::displayQuestion, RxUtils::onError);

        subscriptions.add(subscription);

    }

    private void displayQuestion(Player player) {

        gameNavigator.showQuestion(player);

    }

    public void skipQuestion() {

        startNextQuestion();
    }

    public void finishQuestion(Player p) {

        playerRepository.markFinished(p);

        startNextQuestion();
    }

    public void quitGame() {

        gameNavigator.quitGame();

    }

    public void finishGame() {

        playerRepository.close();
        RxUtils.safeUnsubscribe(subscriptions);

    }
}
