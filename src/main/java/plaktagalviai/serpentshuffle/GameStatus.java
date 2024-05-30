package plaktagalviai.serpentshuffle;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class GameStatus {
    private int gameScore;
    private final Text scoreText;

    public GameStatus() {
        this.gameScore = 0;
        this.scoreText = new Text("Apples: 0");
        scoreText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 3, 1, 0, 0); -fx-fill: white;");
    }


    public void incrementScore() {
        gameScore++;
        scoreText.setText("Apples: " + gameScore);
    }

    public Text getScoreText() {
        return scoreText;
    }

    public int getGameScore() {
        return gameScore;
    }
}

