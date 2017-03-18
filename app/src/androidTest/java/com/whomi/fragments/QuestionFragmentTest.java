package com.whomi.fragments;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.core.deps.guava.collect.Lists;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.whomi.Key;
import com.whomi.R;
import com.whomi.TestApplication;
import com.whomi.activities.GameActivity;
import com.whomi.model.objects.Player;
import com.whomi.model.objects.TeamHistory;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;
import sharedTest.PlayerHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.whomi.utils.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuestionFragmentTest {

    private final Player dummyPlayer = PlayerHelper.getDummyPlayer();

    @Rule
    public ActivityTestRule<GameActivity> activityTestRule = new ActivityTestRule<>(GameActivity.class, false, false);

    @BeforeClass
    public static void setup() {

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestApplication application = (TestApplication) instrumentation.getTargetContext().getApplicationContext();



    }

    @Before
    public void setupTest() throws Exception {

        System.out.println("Hello");

        activityTestRule.launchActivity(null);

        activityTestRule.getActivity().showQuestion(dummyPlayer);

    }

    @After
    public void cleanTest() throws Exception {

    }

    @Test
    public void testGuessesObservable() throws Exception {

        final QuestionFragment questionFragment = getQuestionFragment();

        final TestSubscriber<Key> testSubscriber = TestSubscriber.create();

        questionFragment.guesses().subscribe(testSubscriber);

        onView(withId(R.id.question_letters)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        testSubscriber.assertValue(Key.E);
        testSubscriber.assertNoErrors();
        testSubscriber.assertNotCompleted();

    }

    @Test
    public void testSetTeamHistory() throws Exception {

        final QuestionFragment questionFragment = getQuestionFragment();

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
    public void testShowGuess() {

        final QuestionFragment questionFragment = getQuestionFragment();

        questionFragment.setGuess("Hello World");

        onView(withId(R.id.question_answer)).check(matches(withText("Hello World")));

    }

    @Test
    public void testCorrectGuess() {

        final QuestionFragment questionFragment = getQuestionFragment();

        questionFragment.correctGuess(Key.E);

        onView(withRecyclerView(R.id.question_letters).atPosition(2))
                .check(matches(not(isEnabled())));


    }

    @Test
    public void testWrongGuess() {

        final QuestionFragment questionFragment = getQuestionFragment();

        questionFragment.correctGuess(Key.E);

        onView(withRecyclerView(R.id.question_letters).atPosition(2))
                .check(matches(not(isEnabled())));

    }

    @Test
    public void testShowComplete() {

        final QuestionFragment questionFragment = getQuestionFragment();


        questionFragment.showComplete();

        onView(withId(R.id.question_skip)).check(matches(not(isDisplayed())));
        onView(withId(R.id.question_continue)).check(matches(isDisplayed()));


    }

    @Test
    public void testMoveToNext() {

        final QuestionFragment questionFragment = getQuestionFragment();

        assertObservableButtonEvent(R.id.question_continue, questionFragment.moveToNextClick());

    }

    @Test
    public void testSkip() {

        final QuestionFragment questionFragment = getQuestionFragment();

        assertObservableButtonEvent(R.id.question_continue, questionFragment.skipClick());


    }

    private QuestionFragment getQuestionFragment() {
        return (QuestionFragment) activityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.content);
    }


    @Test
    public void testClue() throws Exception {


    }

    private void assertObservableButtonEvent(int id, Observable<Void> obs) {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        obs.subscribe(testSubscriber);

        onView(withId(id)).perform(click());

        testSubscriber.assertValueCount(1);

    }


}