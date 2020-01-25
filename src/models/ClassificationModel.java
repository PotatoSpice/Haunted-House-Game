package models;

public class ClassificationModel {

    private String mapName, playerName;
    private int remainingHP, numberOfMoves, difficulty;

    public ClassificationModel(String mapName, String playerName, int remainingHP, int numberOfMoves, int difficulty) {
        this.mapName = mapName;
        this.playerName = playerName;
        this.remainingHP = remainingHP;
        this.numberOfMoves = numberOfMoves;
        this.difficulty = difficulty;
    }

    public String getMapName() {
        return mapName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getRemainingHP() {
        return remainingHP;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
