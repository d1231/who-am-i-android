package com.danny.projectt;

import com.danny.projectt.model.flow.QuestionScorer;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class QuestionScorerTest {

    private QuestionScorer questionScorer;

    @Before
    public void setUp() throws Exception {

        questionScorer = new QuestionScorer(50, 0);

    }

    @Test
    public void testScoreCorrect() throws Exception {

        int scoreCorrect = questionScorer.scoreCorrect();
        assertThat(scoreCorrect, is(10));
        assertThat(questionScorer.getQuestionScore(), is(60));

        scoreCorrect = questionScorer.scoreCorrect();
        assertThat(scoreCorrect, is(11));
        assertThat(questionScorer.getQuestionScore(), is(71));

    }

    @Test
    public void testScoreIncorrect() throws Exception {


        final int scoreIncorrect = questionScorer.scoreIncorrect();
        assertThat(scoreIncorrect, is(-5));
        assertThat(questionScorer.getQuestionScore(), is(45));
    }

    @Test
    public void testGetScore() throws Exception {


        questionScorer.scoreCorrect();
        assertThat(questionScorer.getQuestionScore(), is(60));

        questionScorer.scoreCorrect();
        assertThat(questionScorer.getQuestionScore(), is(71));

        questionScorer.scoreIncorrect();
        assertThat(questionScorer.getQuestionScore(), is(66));

        questionScorer.scoreCorrect();
        assertThat(questionScorer.getQuestionScore(), is(76));

    }
}