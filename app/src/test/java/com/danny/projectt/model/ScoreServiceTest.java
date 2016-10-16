package com.danny.projectt.model;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static com.danny.projectt.model.ScoreService.PREF_SCORE;
import static com.danny.projectt.model.ScoreService.PREF_SEQUENCE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoreServiceTest {

    @Mock
    SharedPreferences sharedPreferences;

    @InjectMocks
    ScoreService scoreService;

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setUp() throws Exception {

        when(sharedPreferences.getInt(eq(PREF_SCORE), anyInt())).thenReturn(50);
        when(sharedPreferences.getInt(eq(PREF_SEQUENCE), anyInt())).thenReturn(1);

        final SharedPreferences.Editor mockEditor = Mockito.mock(SharedPreferences.Editor.class);

        when(sharedPreferences.edit()).thenReturn(mockEditor);
        when(sharedPreferences.edit().putInt(anyString(), anyInt())).thenReturn(mockEditor);

    }

    @SuppressLint("CommitPrefEdits")
    @Test
    public void testAddQuestionScore() throws Exception {

        scoreService.addQuestionScore(10);

        verify(sharedPreferences.edit(), times(1)).putInt(PREF_SCORE, 10);

    }

    @SuppressLint("CommitPrefEdits")
    @Test
    public void testSetSequence() throws Exception {

        scoreService.setSequence(10);

        verify(sharedPreferences.edit(), times(1)).putInt(PREF_SEQUENCE, 10);


    }

    @Test
    public void testGetCurrentSequence() throws Exception {


        scoreService.setSequence(10);

        assertThat(scoreService.getCurrentSequence(), is(10));

    }

    @Test
    public void testGetTotalScoreObservable() throws Exception {

        final TestSubscriber<Integer> testSubscriber = TestSubscriber.create();

        scoreService.getTotalScoreObservable().subscribe(testSubscriber);

        scoreService.addQuestionScore(10);
        scoreService.addQuestionScore(20);
        scoreService.addQuestionScore(30);

        testSubscriber.assertValues(0, 10, 30, 60);

    }

    @Test
    public void testGetTotalScoreObservableMultiple() throws Exception {

        final TestSubscriber<Integer> testSubscriber = TestSubscriber.create();
        final Subscription subscription = scoreService.getTotalScoreObservable()
                                                      .subscribe(testSubscriber);

        scoreService.addQuestionScore(10);
        scoreService.addQuestionScore(20);
        scoreService.addQuestionScore(30);

        testSubscriber.assertValues(0, 10, 30, 60);

        subscription.unsubscribe();

        final TestSubscriber<Integer> testSubscriber1 = TestSubscriber.create();
        scoreService.getTotalScoreObservable().subscribe(testSubscriber1);

        scoreService.addQuestionScore(10);

        testSubscriber1.assertValues(60, 70);
    }
}