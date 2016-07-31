package com.danny.projectt.model.flow;

public class QuestionScorer {

    private final static int BASE_CORRECT_SCORE = 10;

    private static final int BASE_INCORRECT_SCORE = 5;

    private int correctSequence;

    private int questionScore;

    public QuestionScorer(int baseScore, int correctSequence) {

        this.correctSequence = correctSequence;
        this.questionScore = baseScore;
    }

    public int scoreCorrect() {

        correctSequence += 1;

        final int guessScore = BASE_CORRECT_SCORE + getSequenceScore(correctSequence);

        questionScore += guessScore;

        return guessScore;
    }

    private int getSequenceScore(int correctSequence) {

        return correctSequence - 1;
    }

    public int scoreIncorrect() {

        correctSequence = 0;

        questionScore = Math.max(questionScore - BASE_INCORRECT_SCORE, 0);

        return -BASE_INCORRECT_SCORE;

    }

    public int getQuestionScore() {

        return questionScore;
    }

    public int getCurrentSequence() {

        return correctSequence;
    }
}
