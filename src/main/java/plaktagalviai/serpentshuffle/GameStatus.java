package plaktagalviai.serpentshuffle;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class GameStatus {
    private int gameScore;
    private final Text scoreText;

    public GameStatus() {
        this.gameScore = 0;
        this.scoreText = new Text("SCORE: 0");
        scoreText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 1, 1, 1, 1);");
        scoreText.setFill(Color.WHITE);
    }

    public void incrementScore() {
        gameScore++;
        scoreText.setText("SCORE: " + gameScore);
    }

    public Text getScoreText() {
        return scoreText;
    }

    public int getGameScore() {
        return gameScore;
    }
}

