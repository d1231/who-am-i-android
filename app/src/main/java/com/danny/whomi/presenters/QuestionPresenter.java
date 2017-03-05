package com.danny.whomi.presenters;

import com.danny.whomi.GameController;
import com.danny.whomi.Key;
import com.danny.whomi.services.QuestionManager;
import com.danny.whomi.R;
import com.danny.whomi.fragments.DialogResult;
import com.danny.whomi.services.ClueService;
import com.danny.whomi.model.objects.Player;
import com.danny.whomi.utils.RxUtils;
import com.danny.whomi.views.QuestionBarView;
import com.danny.whomi.views.QuestionView;

import javax.inject.Inject;

import rx.Subscription;

public class QuestionPresenter extends BasePresenter<QuestionView> {

    private final GameController gameController;

    private final ClueService clueService;

    private final Player player;

    private QuestionManager questionManager;

    private QuestionView questionView;

    private boolean guessFinish;

    private QuestionBarView questionBarView;

    @Inject
    QuestionPresenter(GameController gameController, ClueService clueService, Player player) {

        this.gameController = gameController;

        this.clueService = clueService;

        this.player = player;

    }

    @Override
    public void attachView(QuestionView view) {

        questionView = view;
        questionBarView = view.getQuestionBarView();

        questionManager = new QuestionManager(player.name());

        final Subscription guessSubs = questionManager.textObservable()
                                                      .subscribe(view::setGuess, RxUtils::onError, this::onQuestionFinish);

        final Subscription clubNumSub = clueService.getCluesObservable()
                                                   .subscribe(questionBarView::setClues, RxUtils::onError);

        final Subscription guessInputSubs = view.guesses()
                                                .takeWhile(key -> !guessFinish)
                                                .subscribe(this::onGuessedLetter, RxUtils::onError);

        final Subscription clueSubs = questionBarView.clueClick()
                                                     .takeWhile(res -> !guessFinish)
                                                     .subscribe(this::clueClicked, RxUtils::onError);

        final Subscription skipSub = view.skipClick()
                                         .takeWhile(res -> !guessFinish)
                                         .subscribe(res -> skipQuestion(), RxUtils::onError);

        final Subscription nextSub = view.moveToNextClick()
                                         .subscribe(res -> nextQuestion(), RxUtils::onError);

        final Subscription menuSub = questionBarView.menuClick()
                                                    .subscribe(res -> goToMenu(), RxUtils::onError);

        final Subscription questionSub = questionBarView.shareClick()
                                                    .subscribe(res -> goToShare(), RxUtils::onError);

        addSubscriptions(guessInputSubs, guessSubs, clueSubs, nextSub, skipSub, menuSub, clubNumSub, questionSub);

        view.setTeamHistory(player.teamHistory());
    }

    private void goToShare() {


    }

    private void skipQuestion() {

        final Subscription dialogSub = questionView.getDialogBuilder()
                                                   .setTitle(R.string.dialog_skip_title)
                                                   .setMsg(R.string.dialog_skip_msg)
                                                   .setPosMsg(R.string.dialog_skip_pos)
                                                   .setNegMsg(R.string.dialog_skip_neg)
                                                   .create()
                                                   .subscribe(dialogResult -> {

                                                       if (dialogResult == DialogResult.POSITIVE_CLICKED) {
                                                           gameController.skipQuestion();
                                                       }

                                                   }, RxUtils::onError);

        addSubscriptions(dialogSub);
    }

    private void nextQuestion() {

        gameController.startNext();

    }

    private void goToMenu() {

        gameController.quitGame();

        // todo show dialog menu with skip and quit

    }

    @Override
    public void detachView() {

        super.detachView();

        questionView = null;

    }

    private void clueClicked(Void aVoid) {


        final int clues = clueService.getClues();
        if (clues > 0) {
            char letter = questionManager.randomRevealing();
            clueService.clueUsed();
            questionView.correctGuess(Key.get(letter));
        }

    }

    private void onQuestionFinish() {

        guessFinish = true;

        questionView.showComplete();

        gameController.finishQuestion();

    }

    private void onGuessedLetter(Key key) {

        final QuestionManager.GuessResults guessResults = questionManager.guess(key.getLetter());

        if (guessResults.isCorrectGuess()) {
            questionView.correctGuess(key);
        } else {
            questionView.incorrectGuess(key);

        }

    }

}
