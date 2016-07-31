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

import static com.danny.projectt.model.ScoreRepository.PREF_SCORE;
import static com.danny.projectt.model.ScoreRepository.PREF_SEQUENCE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoreRepositoryTest {

    @Mock
    SharedPreferences sharedPreferences;

    @InjectMocks
    ScoreRepository scoreRepository;

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

        scoreRepository.addQuestionScore(10);

        verify(sharedPreferences.edit(), times(1)).putInt(PREF_SCORE, 10);

    }

    @SuppressLint("CommitPrefEdits")
    @Test
    public void testSetSequence() throws Exception {

        scoreRepository.setSequence(10);

        verify(sharedPreferences.edit(), times(1)).putInt(PREF_SEQUENCE, 10);


    }

    @Test
    public void testGetCurrentSequence() throws Exception {


        scoreRepository.setSequence(10);

        assertThat(scoreRepository.getCurrentSequence(), is(10));

    }

    @Test
    public void testGetTotalScoreObservable() throws Exception {

        final TestSubscriber<Integer> testSubscriber = TestSubscriber.create();

        scoreRepository.getTotalScoreObservable().subscribe(testSubscriber);

        scoreRepository.addQuestionScore(10);
        scoreRepository.addQuestionScore(20);
        scoreRepository.addQuestionScore(30);

        testSubscriber.assertValues(0, 10, 30, 60);

    }

    @Test
    public void testGetTotalScoreObservableMultiple() throws Exception {

        final TestSubscriber<Integer> testSubscriber = TestSubscriber.create();
        final Subscription subscription = scoreRepository.getTotalScoreObservable()
                                                         .subscribe(testSubscriber);

        scoreRepository.addQuestionScore(10);
        scoreRepository.addQuestionScore(20);
        scoreRepository.addQuestionScore(30);

        testSubscriber.assertValues(0, 10, 30, 60);

        subscription.unsubscribe();

        final TestSubscriber<Integer> testSubscriber1 = TestSubscriber.create();
        scoreRepository.getTotalScoreObservable().subscribe(testSubscriber1);

        scoreRepository.addQuestionScore(10);

        testSubscriber1.assertValues(60, 70);
    }
}