package com.danny.whomi.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danny.whomi.Key;
import com.danny.whomi.MyApplication;
import com.danny.whomi.R;
import com.danny.whomi.adapters.KeyboardAdapter;
import com.danny.whomi.adapters.TeamHistoryAdapter;
import com.danny.whomi.dagger.game.GameComponent;
import com.danny.whomi.dagger.question.DaggerQuestionComponent;
import com.danny.whomi.dagger.question.QuestionComponent;
import com.danny.whomi.dagger.question.QuestionModule;
import com.danny.whomi.model.objects.Player;
import com.danny.whomi.model.objects.TeamHistory;
import com.danny.whomi.presenters.QuestionPresenter;
import com.danny.whomi.views.QuestionBarView;
import com.danny.whomi.views.QuestionView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.subjects.PublishSubject;

public class QuestionFragment extends BaseFragment implements QuestionView, QuestionBarView, KeyboardAdapter.KeyClickedListener {

    private static final String ARG_PLAYER = "player";

    @BindView(R.id.question_letters)
    RecyclerView keyboardRv;

    @BindView(R.id.question_teamhistory)
    RecyclerView teamhistoryRv;

    @BindView(R.id.question_answer)
    TextView guessView;

    @BindView(R.id.question_bar_share)
    TextView shareView;

    @BindView(R.id.question_bar_clue)
    TextView clueView;

    @BindView(R.id.question_bar_menu)
    View menuView;

    @BindView(R.id.question_continue)
    View nextView;

    @BindView(R.id.question_skip)
    View skipView;

    @Inject
    QuestionPresenter presenter;

    private PublishSubject<Key> guessedLettersSubject = PublishSubject.create();

    private KeyboardAdapter keyboardAdapter;

    private Unbinder unbinder;

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

        return inflater.inflate(R.layout.screen_question_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);


        initKeyboard();

        presenter.attachView(this);

    }

    private void initKeyboard() {

        final Context context = getContext();

        final GridLayoutManager layoutManager = new GridLayoutManager(context, 20, LinearLayoutManager.VERTICAL, false);
        final KeyboardSpanSizeLookup keyboardSpanSizeLookup = new KeyboardSpanSizeLookup();
        layoutManager.setSpanSizeLookup(keyboardSpanSizeLookup);

        keyboardRv.setLayoutManager(layoutManager);

        keyboardRv.setItemAnimator(new DefaultItemAnimator());

        keyboardRv.addItemDecoration(new KeyboardItemDecoration());

        keyboardAdapter = new KeyboardAdapter(context, this);
        keyboardRv.setAdapter(keyboardAdapter);
        keyboardRv.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        presenter.detachView();

        unbinder.unbind();

        keyboardAdapter.setListener(null);

    }

    @Override
    public QuestionBarView getQuestionBarView() {

        return this;
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
    public Observable<Void> skipClick() {

        return RxView.clicks(skipView);
    }

    @Override
    public void setClues(int clues) {

        clueView.setText(getString(R.string.question_bar_clue, clues));

    }

    @Override
    public Observable<Void> shareClick() {

        return RxView.clicks(shareView);
    }

    @Override
    public Observable<Void> clueClick() {

        return RxView.clicks(clueView);
    }

    @Override
    public Observable<Void> menuClick() {

        return RxView.clicks(menuView);
    }

    @Override
    public void onKeyClicked(Key key) {

        guessedLettersSubject.onNext(key);

    }


    private static class KeyboardItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            final int position = parent.getChildLayoutPosition(view);

            final int width = parent.getMeasuredWidth() / 20;

            int left = 0;
            int right = 0;
            if (position == Key.A.ordinal() || position == Key.L.ordinal()) {

                if (position == Key.A.ordinal()) {
                    left = width;
                } else {
                    right = width;
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

    }

    private static class KeyboardSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

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
    }
}
