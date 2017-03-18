package com.whomi.services;

import com.whomi.utils.CommonStringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

public class QuestionManager {

    private StringBuilder sb;

    private final BehaviorSubject<String> currentGuessSubject;

    private final Map<Character, List<Integer>> letters = Maps.newHashMap();


    private List<Character> game = Lists.newArrayList();

    public QuestionManager(String name) {
        this(name, "");

    }

    private void initQuestionManager(String displayName, Set<Character> revealedChars) {

        String normalizedDisplayName = NameService.instance().normalizeName(displayName);

        final int length = normalizedDisplayName.length();

        sb = new StringBuilder(length);

        int k = 0;
        for (int i = 0; i < length; i++) {

            final char c = normalizedDisplayName.charAt(i);
            char upperCase = Character.toUpperCase(c);

            if (revealedChars.contains(upperCase)) {

                sb.append(revealedChars);

            } else if (Character.isLetter(upperCase)) {
                sb.append('*');

                List<Integer> indices = this.letters.get(upperCase);
                if (indices == null) {
                    indices = Lists.newArrayList();
                    this.letters.put(upperCase, indices);
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

        initQuestionManager(name, CommonStringUtils.getLettersSetFromString(currentGuess));

        this.currentGuessSubject = BehaviorSubject.create(sb.toString());

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

        game.add('+');

        Random rnd = new Random();

        final int index = rnd.nextInt(letters.size());

        final Object randomChar = letters.keySet().toArray()[index];

        final char c = (Character) randomChar;

        reveal(c);

        publishResults(c);

        return c;
    }

    private int reveal(char c) {

        game.add(c);

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

        Timber.d("guess() called with: " + "c = [" + c + "]");

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

    public List<Character> getGame() {
        return game;
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
