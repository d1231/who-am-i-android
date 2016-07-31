package com.danny.projectt;

import com.danny.projectt.model.PlayerRepository;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.navigator.GameNavigator;
import com.danny.projectt.utils.RxUtils;

import rx.android.schedulers.AndroidSchedulers;

public class GameController {

    private static final int BASE_SCORE = 50;

    private final GameNavigator gameNavigator;

    private final PlayerRepository playerRepository;

    public GameController(PlayerRepository playerRepository, GameNavigator gameNavigator) {

        this.playerRepository = playerRepository;
        this.gameNavigator = gameNavigator;
    }

    public void startGame() {

        startNextQuestion();

    }

    private void startNextQuestion() {

        playerRepository.getPlayer()
                        .subscribe(this::displayQuestion, RxUtils::onError);
    }

    private void displayQuestion(Player player) {

        gameNavigator.showQuestion(player);

    }

    public void finishQuestion() {

        startNextQuestion();
    }

    public void quitGame() {

        gameNavigator.quitGame();

    }
}
