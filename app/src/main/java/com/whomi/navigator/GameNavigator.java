package com.whomi.navigator;

import com.whomi.model.objects.Player;

public interface GameNavigator {

    void quitGame();

    void showQuestion(Player player);

    void showLoading();

    void hideLoading();

    void quitGameWithError();

}
