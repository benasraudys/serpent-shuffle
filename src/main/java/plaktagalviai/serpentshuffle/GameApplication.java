package plaktagalviai.serpentshuffle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.util.Random;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Window;

public class GameApplication extends Application {

    private static final int WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 720;
    private static final int SCENE_SIZE = WINDOW_HEIGHT;
    private static final int GRID_SUBDIVISIONS = 10;
    private static final int SUBDIVISION_LENGTH = SCENE_SIZE / GRID_SUBDIVISIONS;
    private static final int MOVE_TIME_MILLISECONDS = 180;

    private final Snake snake = new Snake();
    private Apple apple;
    private GameStatus gameStatus;
    private KeyCode lastKeyCode;

    int cayoteTime = 0; // Gives a small grace period before hitting wall or self

    Timeline timeline;

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        Pane gamePane = initializePane();
        root.getChildren().add(gamePane);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        initializeSnake(gamePane);
        initializeApple(gamePane);
        initializeScore(root);

        initializeTimeline(gamePane);

        scene.setOnKeyPressed(event -> updateSnakeDirection(event.getCode()));

        stage.setTitle("Serpent Shuffle");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private Pane initializePane() {
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

    private void initializeSnake(Pane root) {
        int centerCoordinate = (int) (GRID_SUBDIVISIONS / 2f);
        SnakeSegment initialSegment = new SnakeSegment(centerCoordinate, centerCoordinate, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
        Image image = new Image(getClass().getResourceAsStream("snake-head.png"));
        initialSegment.getRectangle().setFill(new ImagePattern(image));
        snake.add(initialSegment);
        root.getChildren().add(initialSegment.getRectangle());
        snake.addSegment(root, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
        snake.addSegment(root, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
    }

    private void initializeApple(Pane root) {
        apple = createApple();
        root.getChildren().add(apple.getRectangle());
    }

    private Apple createApple() {
        Random rand = new Random();
        double x = rand.nextInt(GRID_SUBDIVISIONS);
        double y = rand.nextInt(GRID_SUBDIVISIONS);
        for (SnakeSegment segment : snake) {
            if( x == segment.getX() && y == segment.getY()) {
                return createApple();
            }
        }

        return new Apple(x, y, SUBDIVISION_LENGTH);
    }

    private void initializeScore(StackPane root) {
        gameStatus = new GameStatus();
        StackPane.setAlignment(gameStatus.getScoreText(), Pos.TOP_CENTER);
        gameStatus.getScoreText().setTranslateY(30);
        root.getChildren().add(gameStatus.getScoreText());
    }

    private void initializeTimeline(Pane root) {
        timeline = new Timeline(new KeyFrame(Duration.millis(MOVE_TIME_MILLISECONDS), e -> updateSnake(root)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateSnakeDirection(KeyCode keyCode) {
        if (snake.isEmpty() || (lastKeyCode == KeyCode.UP && keyCode == KeyCode.DOWN)
                || (lastKeyCode == KeyCode.DOWN && keyCode == KeyCode.UP)
                || (lastKeyCode == KeyCode.LEFT && keyCode == KeyCode.RIGHT)
                || (lastKeyCode == KeyCode.RIGHT && keyCode == KeyCode.LEFT)) {
            return;
        }

        double dx = 0, dy = 0;

        switch (keyCode) {
            case UP:
                dy = -1;
                break;
            case DOWN:
                dy = 1;
                break;
            case LEFT:
                dx = -1;
                break;
            case RIGHT:
                dx = 1;
                break;
            case SPACE:
                snake.addSegment((Pane) snake.getFirst().getRectangle().getParent(), SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
                break;
        }

        lastKeyCode = keyCode;

        if (dx != 0 || dy != 0) {
            snake.getHead().setDirection(dx, dy);
        }
    }

    private void updateSnake(Pane root) {
        //snake.move();
        if (snake.willCollideWithWall(GRID_SUBDIVISIONS) || snake.willCollideWithSelf()) {
            if (cayoteTime > 2) {
                snake.move();
                cayoteTime = 0;
            }
            else {
                cayoteTime++;
                return;
            }
        } else {
            snake.move();
            cayoteTime = 0;
        }

        if (apple.isEatenBySnake(snake.getHead())) {
            eatTheApple(root);
        }

        if (snake.collidedWithWall(GRID_SUBDIVISIONS) || snake.collidedWithSelf()) {
            gameOver();
        }
    }

    private void eatTheApple(Pane root) {
        root.getChildren().remove(apple.getRectangle());
        apple = createApple();
        root.getChildren().add(apple.getRectangle());
        apple.getRectangle().toBack();
        snake.addSegment(root, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
        gameStatus.incrementScore();
    }

    private void gameOver() {
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
}
