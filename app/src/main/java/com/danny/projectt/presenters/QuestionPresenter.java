package com.danny.projectt.presenters;

import com.danny.projectt.GameController;
import com.danny.projectt.Key;
import com.danny.projectt.R;
import com.danny.projectt.fragments.DialogResult;
import com.danny.projectt.model.ClueService;
import com.danny.projectt.model.ScoreService;
import com.danny.projectt.QuestionManager;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.utils.RxUtils;
import com.danny.projectt.views.QuestionBarView;
import com.danny.projectt.views.QuestionView;

import javax.inject.Inject;

import rx.Subscription;

public class QuestionPresenter extends BasePresenter<QuestionView> {

    private static final int BASE_SCORE = 50;

    private final ScoreService scoreService;

    private final GameController gameController;

    private final ClueService clueService;

    private final Player player;

    private QuestionManager questionManager;

    private QuestionView questionView;

    private boolean guessFinish;

    private QuestionBarView questionBarView;

    @Inject
    QuestionPresenter(GameController gameController, ScoreService scoreService, ClueService clueService, Player player) {

        this.gameController = gameController;

        this.clueService = clueService;

        this.player = player;

        this.scoreService = scoreService;

    }

    @Override
    public void attachView(QuestionView view) {

        questionView = view;
        questionBarView = view.getQuestionBarView();

        questionManager = new QuestionManager(player.name(), BASE_SCORE, scoreService.getCurrentSequence());

        final Subscription guessSubs = questionManager.textObservable()
                                                      .subscribe(view::setGuess, RxUtils::onError, this::guessFinish);

        final Subscription totalScoreSub = scoreService.getTotalScoreObservable()
                                                       .subscribe(questionBarView::showTotalScore, RxUtils::onError);

        final Subscription clubNumSub = clueService.getCluesObservable()
                                                   .subscribe(questionBarView::setClues, RxUtils::onError);

        final Subscription guessInputSubs = view.guesses()
                                                .takeWhile(key -> !guessFinish)
                                                .subscribe(this::guessLetter, RxUtils::onError);

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

        addSubscriptions(guessInputSubs, guessSubs, clueSubs, nextSub, skipSub, menuSub, totalScoreSub, clubNumSub);

        view.updateQuestionScore(questionManager.getQuestionScore());

        view.setTeamHistory(player.teamHistory());
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
                                                           scoreService.setSequence(0);
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

    private void guessFinish() {

        guessFinish = true;

        questionView.showComplete();

        gameController.finishQuestion();

        scoreService.setSequence(questionManager.getCorrectSequence());

        scoreService.addQuestionScore(questionManager.getQuestionScore());
    }

    private void guessLetter(Key key) {

        final QuestionManager.GuessResults guessResults = questionManager.guess(key.getLetter());

        if (guessResults.isCorrectGuess()) {
            questionView.correctGuess(key);
        } else {
            questionView.incorrectGuess(key);

        }

        final int guessScore = guessResults.getGuessPoints();
        final int totalPoints = guessResults.getTotalPoints();
        questionView.updateQuestionScore(totalPoints, guessScore);
    }

}
