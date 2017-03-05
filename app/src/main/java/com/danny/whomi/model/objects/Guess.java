package com.danny.whomi.model.objects;

public class Guess {

    private String playerId;

    private String playerName;

    private String guess;

    public Guess() {

    }

    public Guess(String playerId, String playerName, String guess) {

        this.playerId = playerId;
        this.playerName = playerName;
        this.guess = guess;
    }

    public String getPlayerId() {

        return playerId;
    }

    public void setPlayerId(String playerId) {

        this.playerId = playerId;
    }

    public String getPlayerName() {

        return playerName;
    }

    public void setPlayerName(String playerName) {

        this.playerName = playerName;
    }

    public String getGuess() {

        return guess;
    }

    public void setGuess(String guess) {

        this.guess = guess;
    }

    @Override
    public int hashCode() {

        int result = playerId != null ? playerId.hashCode() : 0;
        result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
        result = 31 * result + (guess != null ? guess.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Guess guess1 = (Guess) o;

        if (playerId != null ? !playerId.equals(guess1.playerId) : guess1.playerId != null)
            return false;
        if (playerName != null ? !playerName.equals(guess1.playerName) : guess1.playerName != null)
            return false;
        return guess != null ? guess.equals(guess1.guess) : guess1.guess == null;

    }

    @Override
    public String toString() {

        return "Guess{" +
                "playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", guess='" + guess + '\'' +
                '}';
    }
}
