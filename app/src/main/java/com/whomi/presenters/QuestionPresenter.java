package com.whomi.presenters;

import com.whomi.Key;
import com.whomi.R;
import com.whomi.analytics_events.NextQuestionEvent;
import com.whomi.analytics_events.PlayerGuessEvent;
import com.whomi.analytics_events.SharePlayerEvent;
import com.whomi.analytics_events.SkipPlayerEvent;
import com.whomi.fragments.DialogResult;
import com.whomi.model.objects.Player;
import com.whomi.services.ClueService;
import com.whomi.services.QuestionManager;
import com.whomi.services.ShareService;
import com.whomi.services.WhomiAnalyticsService;
import com.whomi.utils.RxUtils;
import com.whomi.views.QuestionBarView;
import com.whomi.views.QuestionView;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscription;
import timber.log.Timber;

public class QuestionPresenter extends BasePresenter<QuestionView> {

    private final GameController gameController;

    private final ClueService clueService;

    private ShareService shareService;

    private WhomiAnalyticsService whomiAnalyticsService;

    private final Player player;

    private QuestionManager questionManager;

    private QuestionView questionView;

    private boolean guessFinish;

    @Inject
    QuestionPresenter(GameController gameController, ClueService clueService, ShareService shareService, WhomiAnalyticsService whomiAnalyticsService, Player player) {

        this.gameController = gameController;

        this.clueService = clueService;
        this.shareService = shareService;
        this.whomiAnalyticsService = whomiAnalyticsService;

        this.player = player;

    }

    @Override
    public void attachView(QuestionView view) {

        questionView = view;

        QuestionBarView questionBarView = view.getQuestionBarView();

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

        final Subscription shareSub = questionBarView.shareClick()
                .subscribe(res -> goToShare(), RxUtils::onError);

        addSubscriptions(guessInputSubs, guessSubs, clueSubs, nextSub, skipSub, menuSub, clubNumSub, shareSub);

        view.setTeamHistory(player.teamHistory());
    }

    @Override
    public void detachView() {

        super.detachView();

        questionView = null;

    }

    private void goToShare() {

        Timber.d("goToShare() called with: " + "");

        whomiAnalyticsService.logEvent(SharePlayerEvent.create(player));

        try {
            shareService.sharePlayer(player);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void skipQuestion() {

        Timber.d("skipQuestion() called with: " + "");

        final Subscription dialogSub = questionView.getDialogBuilder()
                .setTitle(R.string.dialog_skip_title)
                .setMsg(R.string.dialog_skip_msg)
                .setPosMsg(R.string.dialog_skip_pos)
                .setNegMsg(R.string.dialog_skip_neg)
                .create()
                .doOnNext(res -> whomiAnalyticsService.logEvent(SkipPlayerEvent.create(player, res, questionManager.getGame())))
                .subscribe(dialogResult -> {

                    if (dialogResult == DialogResult.POSITIVE_CLICKED) {
                        gameController.skipQuestion();
                    }


                }, RxUtils::onError);

        addSubscriptions(dialogSub);
    }

    private void nextQuestion() {

        Timber.d("nextQuestion() called with: " + "");

        whomiAnalyticsService.logEvent(NextQuestionEvent.create());

        gameController.startNext();

    }

    private void goToMenu() {

        Timber.d("goToMenu() called with: " + "");

        gameController.quitGame();

        // todo show dialog menu with skip and quit

    }

    private void clueClicked(Void aVoid) {

        Timber.d("clueClicked() called with: " + "aVoid = [" + aVoid + "]");

        whomiAnalyticsService.logEvent(NextQuestionEvent.create());

        final int clues = clueService.getClues();
        if (clues > 0) {
            char letter = questionManager.randomRevealing();
            clueService.clueUsed();
            questionView.correctGuess(Key.get(letter));
        }

    }

    private void onQuestionFinish() {

        Timber.d("onQuestionFinish() called with: " + "");

        whomiAnalyticsService.logEvent(PlayerGuessEvent.create(player, questionManager.getGame()));

        guessFinish = true;

        questionView.showComplete();

        gameController.finishQuestion();

    }

    private void onGuessedLetter(Key key) {

        Timber.d("onGuessedLetter() called with: " + "key = [" + key + "]");

        final QuestionManager.GuessResults guessResults = questionManager.guess(key.getLetter());

        if (guessResults.isCorrectGuess()) {
            questionView.correctGuess(key);
        } else {
            questionView.incorrectGuess(key);

        }

    }

}
