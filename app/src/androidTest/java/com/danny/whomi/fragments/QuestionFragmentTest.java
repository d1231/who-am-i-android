package com.danny.whomi.fragments;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.core.deps.guava.collect.Lists;
import android.support.test.runner.AndroidJUnit4;

import com.danny.whomi.FragmentTestRule;
import com.danny.whomi.Key;
import com.danny.whomi.R;
import com.danny.whomi.TestActivity;
import com.danny.whomi.TestApplication;
import com.danny.whomi.dagger.QuestionFragmentTestComponent;
import com.danny.whomi.model.objects.Player;
import com.danny.whomi.model.objects.TeamHistory;
import com.danny.whomi.views.QuestionView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.observers.TestSubscriber;
import sharedTest.PlayerHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.danny.whomi.Utils.RecyclerViewMatcher.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class QuestionFragmentTest {

    private final Player dummyPlayer = PlayerHelper.getDummyPlayer();

    @Rule
    public FragmentTestRule<QuestionFragment> fragmentRule = new FragmentTestRule<>(QuestionFragment.class,
            () -> QuestionFragment.newInstance(dummyPlayer)
    );

    @Before
    public void setUp() throws Exception {

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestApplication application = (TestApplication) instrumentation.getTargetContext().getApplicationContext();
        ((QuestionFragmentTestComponent) application.getApplicationComponent()).inject(this);

        fragmentRule.launchActivity(null);


    }

    @Test
    public void testGuessesObservable() throws Exception {

        final TestActivity activity = fragmentRule.getActivity();

        final QuestionFragment questionFragment = fragmentRule.getFragment();

        final TestSubscriber<Key> testSubscriber = TestSubscriber.create();

        questionFragment.guesses().subscribe(testSubscriber);

        onView(withId(R.id.question_letters)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        testSubscriber.assertValue(Key.E);
        testSubscriber.assertNoErrors();
        testSubscriber.assertNotCompleted();

    }

    @Test
    public void testSetTeamHistory() throws Exception {

        final QuestionView questionFragment = fragmentRule.getFragment();

        final List<TeamHistory> teamHistories = PlayerHelper.getDummyPlayer().teamHistory();

        questionFragment.setTeamHistory(Lists.newArrayList(teamHistories));

        final TeamHistory teamHistory = teamHistories.get(0);

        onView(withRecyclerView(R.id.question_teamhistory).atPositionOnView(0, R.id.team_history_years))
                .check(matches(withText(String.format("%s-%s", teamHistory.startYear(), teamHistory.endYear()))));

        onView(withRecyclerView(R.id.question_teamhistory).atPositionOnView(0, R.id.team_history_name))
                .check(matches(withText("" + teamHistory.teamName())));

        onView(withRecyclerView(R.id.question_teamhistory).atPositionOnView(0, R.id.team_history_goals))
                .check(matches(withText("" + teamHistory.leagueStats().goals())));

        onView(withRecyclerView(R.id.question_teamhistory).atPositionOnView(0, R.id.team_history_apps))
                .check(matches(withText("" + teamHistory.leagueStats().apps())));

    }


    @Test
    public void testClue() throws Exception {

        final QuestionView questionFragment = fragmentRule.getFragment();


    }


}