package com.danny.projectt.services;

import com.danny.projectt.utils.CommonStringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class QuestionManager {

    private final StringBuilder sb;

    private final BehaviorSubject<String> currentGuessSubject;

    private final Map<Character, List<Integer>> letters = Maps.newHashMap();

    private String currentGuess;

    public QuestionManager(String name) {

        final int length = name.length();
        this.sb = new StringBuilder(length);

        createLettersMap(name);

        this.currentGuessSubject = BehaviorSubject.create(sb.toString());

    }

    private void createLettersMap(String displayName) {

        createLettersMap(displayName, Sets.newHashSet());
    }

    private void createLettersMap(String displayName, Set<Character> revealedChars) {

        final int length = displayName.length();

        int k = 0;
        for (int i = 0; i < length; i++) {
            final char c = displayName.charAt(i);

            if (revealedChars.contains(c)) {

                sb.append(revealedChars);

            } else if (Character.isLetter(c)) {
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

    public QuestionManager(String name, String currentGuess) {

        name = StringUtils.stripAccents(name);
        String displayName = name.toUpperCase();

        final int length = displayName.length();
        this.sb = new StringBuilder(length);

        createLettersMap(displayName, CommonStringUtils.getLettersSetFromString(currentGuess));

        this.currentGuessSubject = BehaviorSubject.create(currentGuess);

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

            return GuessResults.create(false);

        } else {

            publishResults(c);

            return GuessResults.create(true);
        }


    }

    private boolean isFinishGuess() {

        return letters.isEmpty();
    }

    public String getCurrentGuess() {

        return sb.toString();
    }

    public static class GuessResults {

        final boolean correctGuess;

        private GuessResults(boolean correctGuess) {

            this.correctGuess = correctGuess;
        }

        public static GuessResults create(boolean correctGuess) {

            return new GuessResults(correctGuess);
        }

        public boolean isCorrectGuess() {

            return correctGuess;
        }

        @Override
        public int hashCode() {

            return (correctGuess ? 1 : 0);
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GuessResults that = (GuessResults) o;

            return correctGuess == that.correctGuess;

        }

        @Override
        public String toString() {

            return "GuessResults{" +
                    "correctGuess=" + correctGuess +
                    '}';
        }
    }
}
