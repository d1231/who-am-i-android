package com.danny.projectt.model.flow;

import com.danny.projectt.Key;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class QuestionManager {

    private static final int POINTS_BAD_GUESS = -5;

    private static final int POINTS_GOOD_GUESS = 10;

    private final StringBuilder sb;

    private final BehaviorSubject<String> currentGuessSubject;

    private final Map<Character, List<Integer>> letters = Maps.newHashMap();

    private int questionScore;

    private int correctSequence;

    public QuestionManager(String name, int questionScore, int correctSequence) {

        this.questionScore = questionScore;
        this.correctSequence = correctSequence;

        name = StringUtils.stripAccents(name);
        String displayName = name.toUpperCase();

        final int length = displayName.length();
        this.sb = new StringBuilder(length);

        createLettersMap(displayName);

        this.currentGuessSubject = BehaviorSubject.create(sb.toString());

    }

    private void createLettersMap(String displayName) {

        final int length = displayName.length();

        int k = 0;
        for (int i = 0; i < length; i++) {
            final char c = displayName.charAt(i);
            if (Character.isLetter(c)) {
                sb.append('*');

                List<Integer> indices = this.letters.get(c);
                if (indices == null) {
                    indices = Lists.newArrayList();
                    this.letters.put(c, indices);
                }

                indices.add(k);

                k++;
            } else if (c == ' ') {

                sb.append(c);
                k++;
            }
        }
    }

    public int getCorrectSequence() {

        return correctSequence;
    }

    public Observable<String> textObservable() {

        return currentGuessSubject;
    }

    public String text() {

        return sb.toString();

    }

    public char randomRevealing() {

        if (letters.isEmpty()) {
            throw new IllegalStateException("No letters to reveal");
        }

        Random rnd = new Random();

        final int index = rnd.nextInt(letters.size());

        final Object randomChar = letters.keySet().toArray()[index];

        final char c = (Character) randomChar;

        reveal(c);

        publishResults(c);

        return c;
    }

    private int reveal(char c) {

        final List<Integer> indices = letters.get(c);

        if (indices == null) {
            return 0;
        }

        for (Integer index : indices) {
            sb.setCharAt(index, c);
        }

        return indices.size();
    }

    private void publishResults(char c) {

        letters.remove(c);

        currentGuessSubject.onNext(sb.toString());

        if (letters.isEmpty()) {
            currentGuessSubject.onCompleted();
        }
    }

    public GuessResults guess(char c) {

        if (!Character.isLetter(c)) {
            throw new IllegalStateException("Not a letter");
        }

        if (isFinishGuess()) {
            throw new IllegalStateException("Finish guessing");
        }

        c = Character.toUpperCase(c);

        int lettersRevealed = reveal(c);

        if (lettersRevealed == 0) {

            int guessScore = scoreBad();
            return GuessResults.create(false, guessScore, questionScore, correctSequence);

        } else {

            int guessScore = scoreGood(lettersRevealed);

            publishResults(c);

            return GuessResults.create(true, guessScore, questionScore, correctSequence);
        }


    }

    private boolean isFinishGuess() {

        return letters.isEmpty();
    }

    private int scoreBad() {

        correctSequence = 0;

        int beforeQuestionScore = questionScore;

        questionScore = Math.max(questionScore + POINTS_BAD_GUESS, 0);

        return questionScore - beforeQuestionScore;

    }

    private int scoreGood(int lettersRevealed) {

        correctSequence += 1;

        final int guessScore = (POINTS_GOOD_GUESS + (correctSequence - 1)) * lettersRevealed;

        questionScore += guessScore;

        return guessScore;
    }

    public int getQuestionScore() {

        return questionScore;
    }

    public static class GuessResults {

        final boolean correctGuess;

        final int guessPoints;

        final int totalPoints;

        final int currentSequence;

        private GuessResults(boolean correctGuess, int guessPoints, int totalPoints, int currentSequence) {

            this.correctGuess = correctGuess;
            this.guessPoints = guessPoints;
            this.totalPoints = totalPoints;
            this.currentSequence = currentSequence;
        }

        public static GuessResults create(boolean correctGuess, int guessPoints, int totalPoints, int currentSequence) {

            return new GuessResults(correctGuess, guessPoints, totalPoints, currentSequence);
        }

        public boolean isCorrectGuess() {

            return correctGuess;
        }

        public int getGuessPoints() {

            return guessPoints;
        }

        public int getTotalPoints() {

            return totalPoints;
        }

        public int getCurrentSequence() {

            return currentSequence;
        }

        @Override
        public int hashCode() {

            int result = (correctGuess ? 1 : 0);
            result = 31 * result + guessPoints;
            result = 31 * result + totalPoints;
            result = 31 * result + currentSequence;
            return result;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GuessResults that = (GuessResults) o;

            if (correctGuess != that.correctGuess) return false;
            if (guessPoints != that.guessPoints) return false;
            if (totalPoints != that.totalPoints) return false;
            return currentSequence == that.currentSequence;

        }

        @Override
        public String toString() {

            return "GuessResults{" +
                    "correctGuess=" + correctGuess +
                    ", guessPoints=" + guessPoints +
                    ", totalPoints=" + totalPoints +
                    ", currentSequence=" + currentSequence +
                    '}';
        }
    }
}
