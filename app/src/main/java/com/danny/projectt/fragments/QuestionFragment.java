package com.danny.projectt.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.danny.projectt.Key;
import com.danny.projectt.MyApplication;
import com.danny.projectt.R;
import com.danny.projectt.adapters.KeyboardAdapter;
import com.danny.projectt.adapters.TeamHistoryAdapter;
import com.danny.projectt.dagger.game.GameComponent;
import com.danny.projectt.dagger.question.DaggerQuestionComponent;
import com.danny.projectt.dagger.question.QuestionComponent;
import com.danny.projectt.dagger.question.QuestionModule;
import com.danny.projectt.model.objects.Player;
import com.danny.projectt.model.objects.TeamHistory;
import com.danny.projectt.presenters.QuestionPresenter;
import com.danny.projectt.views.QuestionView;
import com.jakewharton.rxbinding.view.RxView;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.subjects.PublishSubject;

public class QuestionFragment extends BaseFragment implements QuestionView, KeyboardAdapter.KeyClickedListener {

    private static final String ARG_PLAYER = "player";

    private static final int MSG_UPDATE_QUESTION_SCORE = 5;

    private static final long TIME_UPDATE_QUESTION_SCORE_DELAY = 500;

    @BindView(R.id.question_letters)
    RecyclerView keyboardRv;

    @BindView(R.id.question_teamhistory)
    RecyclerView teamhistoryRv;

    @BindView(R.id.question_answer)
    TextView guessView;

    @BindView(R.id.question_total_score)
    TextView totalScoreTv;

    @BindView(R.id.question_clue)
    View clueView;

    @BindView(R.id.question_menu)
    View menuView;

    @BindView(R.id.question_clue_num)
    TextView clueNumTextView;

    @BindView(R.id.question_score)
    TextSwitcher scoreTextSwitcher;


    @BindView(R.id.question_continue)
    View nextView;

    @BindView(R.id.question_skip)
    View skipView;

    @Inject
    QuestionPresenter presenter;

    private Handler handler = new ScoreUpdaterHandler(this);

    private PublishSubject<Key> guessedLettersSubject = PublishSubject.create();

    private KeyboardAdapter keyboardAdapter;

    private Unbinder unbinder;

    public QuestionFragment() {

    }

    public static QuestionFragment newInstance(Player player) {

        QuestionFragment fragment = new QuestionFragment();

        Bundle args = new Bundle();

        args.putParcelable(ARG_PLAYER, player);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            initDependencies();

        } else {

            throw new IllegalStateException("No player");
        }
    }

    private void initDependencies() {

        final Player player = getArguments().getParcelable(ARG_PLAYER);


        final GameComponent gameComponent = MyApplication.get(getContext()).getGameComponent();

        final QuestionModule questionModule = new QuestionModule(player);

        final QuestionComponent questionComponent = DaggerQuestionComponent.builder()
                                                                           .gameComponent(gameComponent)
                                                                           .questionModule(questionModule)
                                                                           .build();

        questionComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.screen_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        scoreTextSwitcher.setFactory(() -> {

            TextView myText = new TextView(getContext());
            myText.setGravity(Gravity.CENTER);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);

            myText.setLayoutParams(params);

            myText.setTextSize(36);
            myText.setTextColor(Color.BLACK);
            return myText;

        });

        initKeyboard();

        presenter.attachView(this);

    }

    private void initKeyboard() {

        final Context context = getContext();

        final GridLayoutManager layoutManager = new GridLayoutManager(context, 20, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {

                if (position == Key.Z.ordinal() || position == Key.M.ordinal()) {
                    return 5;
                } else if (position == Key.A.ordinal() || position == Key.L.ordinal()) {
                    return 3;
                } else {
                    return 2;
                }
            }
        });

        keyboardRv.setLayoutManager(layoutManager);

        keyboardRv.setItemAnimator(new DefaultItemAnimator());

        keyboardRv.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                final int position = parent.getChildLayoutPosition(view);

                final int width = parent.getMeasuredWidth() / 20;

                int left = 0;
                int right = 0;
                if (position == Key.A.ordinal() || position == Key.L.ordinal()) {

                    final int margin = width;

                    if (position == Key.A.ordinal()) {
                        left = margin;
                    } else {
                        right = margin;
                    }

                    outRect.set(left, 0, right, 0);
                } else if (position == Key.Z.ordinal() || position == Key.M.ordinal()) {

                    final int margin = width * 3;

                    if (position == Key.Z.ordinal()) {
                        left = margin;
                    } else {
                        right = margin;
                    }

                }

                outRect.set(left, 0, right, 0);
            }

        });

        keyboardAdapter = new KeyboardAdapter(context, this);
        keyboardRv.setAdapter(keyboardAdapter);
        keyboardRv.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        presenter.detachView();

        unbinder.unbind();
    }

    @Override
    public void setTeamHistory(List<TeamHistory> teamHistory) {

        Context context = getContext();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        teamhistoryRv.setLayoutManager(linearLayoutManager);

        TeamHistoryAdapter teamHistoryAdapter = new TeamHistoryAdapter(context, teamHistory);
        teamhistoryRv.setAdapter(teamHistoryAdapter);

        teamhistoryRv.setHasFixedSize(true);

    }

    @Override
    public void setNumberOfClues(int integer) {

        clueNumTextView.setText(String.format("%d", integer));

    }

    @Override
    public void updateQuestionScore(int score) {

        scoreTextSwitcher.setCurrentText(String.format("%s", score));

    }

    @Override
    public void updateQuestionScore(int score, int change) {

        if (change > 0) {
            scoreTextSwitcher.setText(String.format("+%s", change));
        } else {
            scoreTextSwitcher.setText(String.format("%s", change));
        }

        final Message msg = ScoreUpdaterHandler.scoreUpdateMessage(handler, score);

        handler.removeMessages(MSG_UPDATE_QUESTION_SCORE);
        handler.sendMessageDelayed(msg, TIME_UPDATE_QUESTION_SCORE_DELAY);
    }

    @Override
    public void showTotalScore(int score) {

        totalScoreTv.setText(String.format("%s", score));

    }

    @Override
    public void setGuess(String guess) {

        guessView.setText(guess);

    }

    @Override
    public void correctGuess(Key key) {

        keyboardAdapter.addCorrectGuess(key);
    }

    @Override
    public void incorrectGuess(Key key) {

        keyboardAdapter.addIncorrectGuess(key);

    }

    @Override
    public void showComplete() {

        nextView.setVisibility(View.VISIBLE);
        skipView.setVisibility(View.GONE);
        scoreTextSwitcher.setVisibility(View.GONE);

    }

    @Override
    public Observable<Key> guesses() {

        return guessedLettersSubject;

    }

    @Override
    public Observable<Void> moveToNextClick() {

        return RxView.clicks(nextView);
    }

    @Override
    public Observable<Void> clueClick() {

        return Observable.merge(RxView.clicks(clueView), RxView.clicks(clueNumTextView));
    }

    @Override
    public Observable<Void> skipClick() {

        return RxView.clicks(skipView);
    }

    @Override
    public Observable<Void> menuClick() {

        return RxView.clicks(menuView);
    }

    @Override
    public void onKeyClicked(Key key) {

        guessedLettersSubject.onNext(key);

    }

    private static class ScoreUpdaterHandler extends Handler {

        private final WeakReference<QuestionFragment> fragWeakRef;

        public ScoreUpdaterHandler(QuestionFragment questionFragment) {

            this.fragWeakRef = new WeakReference<>(questionFragment);
        }

        public static Message scoreUpdateMessage(Handler h, int score) {

            return h.obtainMessage(MSG_UPDATE_QUESTION_SCORE, score, 0);
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_UPDATE_QUESTION_SCORE:
                    final QuestionFragment questionFragment = fragWeakRef.get();

                    if (questionFragment != null) {
                        questionFragment.updateQuestionScore(msg.arg1);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown message");
            }


        }

    }
}
