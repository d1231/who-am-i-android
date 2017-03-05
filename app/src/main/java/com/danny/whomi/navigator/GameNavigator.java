package com.danny.whomi.navigator;

import com.danny.whomi.model.objects.Player;

public interface GameNavigator {

    void quitGame();

    void showQuestion(Player player);

    void showLoading();

    void hideLoading();
}
