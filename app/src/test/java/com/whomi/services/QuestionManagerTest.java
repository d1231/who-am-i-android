package com.whomi.services;

import com.whomi.services.QuestionManager;

import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class QuestionManagerTest {

    @Test
    public void testEncodedString() throws Exception {

        QuestionManager questionManager = new QuestionManager("Robinho");

        assertThat(questionManager.text(), is("*******"));

    }

    @Test
    public void testEncodedStringWithSpaces() throws Exception {

        QuestionManager questionManager = new QuestionManager("Frank Lampard");

        assertThat(questionManager.text(), is("***** *******"));

    }

    @Test
    public void testStringWithAccents() throws Exception {

        QuestionManager questionManager = new QuestionManager("Luiz Cané");

        assertThat(questionManager.text(), is("**** ****"));

        guessName(questionManager, "LuizCaNE");

        assertThat(questionManager.text().toUpperCase(), is("Luiz Cane".toUpperCase()));
    }

    private static void guessName(QuestionManager questionManager, String s) {


        for (int i = 0; i < s.length(); i++) {
            questionManager.guess(s.charAt(i));
        }

    }

    @Test
    public void testStringWithNonLetters() throws Exception {

        QuestionManager questionManager = new QuestionManager("Gary O'Niel");

        assertThat(questionManager.text(), is("**** *****"));

        guessName(questionManager, "GARYONIEL");

        assertThat(questionManager.text().toUpperCase(), is("Gary ONiel".toUpperCase()));
    }

    @Test
    public void testGuess() throws Exception {

        QuestionManager questionManager = new QuestionManager("Frank Lampard");

        assertThat(questionManager.guess('r'), is(QuestionManager.GuessResults.create(true)));
        assertThat(questionManager.text(), is("*R*** *****R*"));

    }

    @Test
    public void testAddCharNotExisting() throws Exception {

        QuestionManager questionManager = new QuestionManager("Frank Lampard");

        assertThat(questionManager.guess('q'), is(QuestionManager.GuessResults.create(false)));
        assertThat(questionManager.text(), is("***** *******"));

    }

    @Test
    public void testAddCharCapitalLetter() throws Exception {

        QuestionManager questionManager = new QuestionManager("Frank Lampard");

        assertThat(questionManager.guess('F'), is(QuestionManager.GuessResults.create(true)));

        assertThat(questionManager.text(), is("F**** *******"));

    }

    @Test
    public void testObservable() throws Exception {

        final TestSubscriber<String> testSubscriber = TestSubscriber.create();
        final TestSubscriber<String> testSubscriber1 = TestSubscriber.create();

        QuestionManager questionManager = new QuestionManager("Frank Lampard");
        final Observable<String> shared = questionManager.textObservable().share();

        shared.subscribe(testSubscriber);
        shared.last().subscribe(testSubscriber1);


        assertThat(questionManager.guess('F'), is(QuestionManager.GuessResults.create(true)));
        assertThat(questionManager.guess('r'), is(QuestionManager.GuessResults.create(true)));
        assertThat(questionManager.guess('q'), is(QuestionManager.GuessResults.create(false)));
        assertThat(questionManager.guess('d'), is(QuestionManager.GuessResults.create(true)));

        testSubscriber.assertValues("***** *******", "F**** *******", "FR*** *****R*", "FR*** *****RD");
        testSubscriber.assertNotCompleted();


        questionManager.guess('a');
        questionManager.guess('n');
        questionManager.guess('k');
        questionManager.guess('l');
        questionManager.guess('m');
        questionManager.guess('p');

        testSubscriber.assertCompleted();

        testSubscriber1.assertValue("FRANK LAMPARD");
        testSubscriber1.assertCompleted();
    }

    @Test
    public void testRandomRevealing() throws Exception {

        QuestionManager questionManager = new QuestionManager("Frank Lampard");

        final String beforeReveal = questionManager.text();

        questionManager.randomRevealing();

        final String afterReveal = questionManager.text();

        assertThat(afterReveal, not(beforeReveal));

    }


}