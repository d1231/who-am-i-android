package com.danny.projectt.presenters;

import com.danny.projectt.GameController;
import com.danny.projectt.Key;
import com.danny.projectt.R;
import com.danny.projectt.fragments.DialogResult;
import com.danny.projectt.model.ClueRepository;
import com.danny.projectt.model.ScoreRepository;
import com.danny.projectt.model.flow.QuestionManager;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.utils.RxUtils;
import com.danny.projectt.views.QuestionView;

import javax.inject.Inject;

import rx.Subscription;

public class QuestionPresenter extends BasePresenter<QuestionView> {

    private static final int BASE_SCORE = 50;

    private final ScoreRepository scoreRepository;

    private final GameController gameController;

    private final ClueRepository clueRepository;

    private final Player player;

    private QuestionManager questionManager;

    private QuestionView view;

    private boolean guessFinish;

    @Inject
    public QuestionPresenter(GameController gameController, ScoreRepository scoreRepository, ClueRepository clueRepository, Player player) {

        this.gameController = gameController;

        this.clueRepository = clueRepository;

        this.player = player;

        this.scoreRepository = scoreRepository;


    }

    @Override
    public void attachView(QuestionView view) {

        this.view = view;

        questionManager = new QuestionManager(player.name(), BASE_SCORE, scoreRepository.getCurrentSequence());

        final Subscription guessSubs = questionManager.textObservable()
                                                      .subscribe(view::setGuess, RxUtils::onError, this::guessFinish);

        final Subscription totalScoreSub = scoreRepository.getTotalScoreObservable()
                                                          .subscribe(view::showTotalScore, RxUtils::onError);

        final Subscription clubNumSub = clueRepository.getCluesObservable()
                                                      .subscribe(view::setNumberOfClues, RxUtils::onError);

        final Subscription guessInputSubs = view.guesses()
                                                .takeWhile(key -> !guessFinish)
                                                .subscribe(this::guessLetter, RxUtils::onError);

        final Subscription clueSubs = view.clueClick()
                                          .takeWhile(res -> !guessFinish)
                                          .subscribe(this::clueClicked, RxUtils::onError);

        final Subscription skipSub = view.skipClick()
                                         .takeWhile(res -> !guessFinish)
                                         .subscribe(res -> skipQuestion(), RxUtils::onError);

        final Subscription nextSub = view.moveToNextClick()
                                         .subscribe(res -> nextQuestion(), RxUtils::onError);

        final Subscription menuSub = view.menuClick()
                                         .subscribe(res -> goToMenu(), RxUtils::onError);

        addSubscriptions(guessInputSubs, guessSubs, clueSubs, nextSub, skipSub, menuSub, totalScoreSub, clubNumSub);

        view.updateQuestionScore(questionManager.getQuestionScore());

        view.setTeamHistory(player.teamHistory());
    }

    private void skipQuestion() {

        final Subscription dialogSub = view.getDialogBuilder()
                                           .setTitle(R.string.dialog_skip_title)
                                           .setMsg(R.string.dialog_skip_msg)
                                           .setPosMsg(R.string.dialog_skip_pos)
                                           .setNegMsg(R.string.dialog_skip_neg)
                                           .create()
                                           .subscribe(dialogResult -> {

                                               if (dialogResult == DialogResult.POSITIVE_CLICKED) {
                                                   scoreRepository.setSequence(0);
                                                   gameController.skipQuestion();
                                               }

                                           }, RxUtils::onError);

        addSubscriptions(dialogSub);
    }

    private void nextQuestion() {

        gameController.finishQuestion(player);
    }

    private void goToMenu() {

        gameController.quitGame();

        // todo show dialog menu with skip and quit

    }

    @Override
    public void detachView() {

        super.detachView();

        view = null;

    }

    private void clueClicked(Void aVoid) {


        final int clues = clueRepository.getClues();
        if (clues > 0) {
            char letter = questionManager.randomRevealing();
            clueRepository.clueUsed();
            view.correctGuess(Key.get(letter));
        }

    }

    private void guessFinish() {

        guessFinish = true;

        view.showComplete();

        scoreRepository.setSequence(questionManager.getCorrectSequence());

        scoreRepository.addQuestionScore(questionManager.getQuestionScore());
    }

    private void guessLetter(Key key) {

        final QuestionManager.GuessResults guessResults = questionManager.guess(key.getLetter());

        if (guessResults.isCorrectGuess()) {
            view.correctGuess(key);
        } else {
            view.incorrectGuess(key);

        }

        final int guessScore = guessResults.getGuessPoints();
        final int totalPoints = guessResults.getTotalPoints();
        view.updateQuestionScore(totalPoints, guessScore);
    }

}
