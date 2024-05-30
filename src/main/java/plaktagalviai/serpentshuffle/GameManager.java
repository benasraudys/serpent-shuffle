package plaktagalviai.serpentshuffle;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.animation.Timeline;

import java.util.Random;

public class GameManager {
    private final int GRID_SUBDIVISIONS;
    private final int SUBDIVISION_LENGTH;
    private Snake snake;
    private Apple apple;
    private GameStatus gameStatus;
    private Timeline timeline;

    public GameManager(int gridSubdivisions, int subdivisionLength) {
        this.GRID_SUBDIVISIONS = gridSubdivisions;
        this.SUBDIVISION_LENGTH = subdivisionLength;
    }

    public Pane initializePane() {
        Pane root = new Pane();
        Image image1 = new Image(getClass().getResourceAsStream("background.png"));
        BackgroundImage backgroundImage = new BackgroundImage(
                image1,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, true)
        );
        root.setBackground(new Background(backgroundImage));
        return root;
    }

    public void initializeSnake(Pane root) {
        snake = new Snake();
        int centerCoordinate = (int) (GRID_SUBDIVISIONS / 2f);
        SnakeSegment initialSegment = new SnakeSegment(centerCoordinate, centerCoordinate, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
        Image image = new Image(getClass().getResourceAsStream("snake-head.png"));
        initialSegment.getRectangle().setFill(new ImagePattern(image));
        snake.add(initialSegment);
        root.getChildren().add(initialSegment.getRectangle());
        snake.addSegment(root, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
        snake.addSegment(root, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
    }

    public void initializeApple(Pane root) {
        apple = createApple();
        root.getChildren().add(apple.getRectangle());
    }

    private Apple createApple() {
        Random rand = new Random();
        int x = rand.nextInt(GRID_SUBDIVISIONS);
        int y = rand.nextInt(GRID_SUBDIVISIONS);
        return new Apple(x, y, SUBDIVISION_LENGTH);
    }

    public void initializeScore(StackPane root) {
        gameStatus = new GameStatus();
        StackPane.setAlignment(gameStatus.getScoreText(), Pos.TOP_CENTER);
        gameStatus.getScoreText().setTranslateY(30);
        root.getChildren().add(gameStatus.getScoreText());
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public void eatTheApple(Pane root) {
        root.getChildren().remove(apple.getRectangle());
        apple = createApple();
        root.getChildren().add(apple.getRectangle());
        snake.addSegment(root, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
        gameStatus.incrementScore();
    }

    public void gameOver() {
        timeline.stop();
        SavedScore savedScore = new SavedScore();
        savedScore.setScore(gameStatus.getGameScore());
        Platform.runLater(() -> {
            Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
            if (stage != null) {
                try {
                    GameOverApplication gameOver = new GameOverApplication();
                    gameOver.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Snake getSnake() {
        return snake;
    }

    public Apple getApple() {
        return apple;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
