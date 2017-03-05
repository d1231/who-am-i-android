package com.danny.whomi.services;

import com.danny.whomi.model.objects.Guess;
import com.danny.whomi.model.objects.Player;
import com.google.common.base.Objects;

import javax.inject.Inject;

public class QuestionService {

    private final NameService nameService;

    private final PersistenceService persistenceService;

    @Inject
    public QuestionService(NameService nameService, PersistenceService persistenceService) {

        this.nameService = nameService;
        this.persistenceService = persistenceService;
    }

    public QuestionManager createQuestionObjectForPlayer(Player player) {

        final String playerId = player.id();

        final Guess currentGuess = persistenceService.getCurrentGuess();


        final QuestionManager questionManager;
        if (Objects.equal(playerId, currentGuess.getPlayerId())) {


            questionManager = new QuestionManager(currentGuess.getPlayerName(), currentGuess.getGuess());
            return questionManager;

        } else {


            final String normalizedName = nameService.normalizeName(player.name());
            questionManager = new QuestionManager(normalizedName);

            persistenceService.setCurrentGuess(new Guess(playerId, normalizedName, questionManager.getCurrentGuess()));

            return questionManager;

        }


    }

}
