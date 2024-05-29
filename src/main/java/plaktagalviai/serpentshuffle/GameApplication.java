package plaktagalviai.serpentshuffle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;


import javafx.scene.text.Text;


import java.util.List;
import java.util.Random;


import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import javafx.stage.Window;

public class GameApplication extends Application {

    private static final int WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 720;
    private static final int SCENE_SIZE = WINDOW_HEIGHT; // Grid, the snake is moving on, size (in pixels) (has to be divisible by GRID_SUBDIVISIONS)
    private static final int GRID_SUBDIVISIONS = 10; // To how many parts the grid is divided to (has to be divisible by 2)
    private static final int SUBDIVISION_LENGTH = SCENE_SIZE / GRID_SUBDIVISIONS;
    private static final int MOVE_TIME_MILLISECONDS = 180; // How fast the snake moves


    private final List<SnakeSegment> snake = new Snake();
    private Apple apple;

    private int gameScore = 0;
    private Text scoreText;
    private KeyCode lastKeyCode;

    Timeline timeline;

    @Override
    public void start(Stage stage) {
        Pane root = initializePane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        initializeSnake(root);
        initializeApple(root);
        initializeScore(root);
        initializeTimeline(root);

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
        addSegment();
        addSegment();
    }

    private void initializeApple(Pane root) {
        addApple();
        root.getChildren().add(apple.getRectangle());
    }

    private void initializeScore(Pane root) {
        scoreText = new Text("SCORE: 0");
        scoreText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 1, 1, 1, 1);");
        scoreText.setFill(Color.WHITE);
        scoreText.setX(320);
        scoreText.setY(30);
        root.getChildren().add(scoreText);
    }

    private void initializeTimeline(Pane root) {
        timeline = new Timeline(new KeyFrame(Duration.millis(MOVE_TIME_MILLISECONDS), e -> updateSnake(root)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void addApple() {
        Random rand = new Random();
        int x = rand.nextInt(GRID_SUBDIVISIONS) * SUBDIVISION_LENGTH;
        int y = rand.nextInt(GRID_SUBDIVISIONS) * SUBDIVISION_LENGTH;
        apple = new Apple(x, y, SUBDIVISION_LENGTH, SUBDIVISION_LENGTH);
    }

    private void updateSnakeDirection(KeyCode keyCode) {
        if (snake.isEmpty() || (lastKeyCode == KeyCode.UP && keyCode == KeyCode.DOWN)
                || (lastKeyCode == KeyCode.DOWN  && keyCode == KeyCode.UP)
                || (lastKeyCode == KeyCode.LEFT  && keyCode == KeyCode.RIGHT)
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
                addSegment();
                break;
        }

        lastKeyCode = keyCode;

        if (dx != 0 || dy != 0) {
            snake.getFirst().setDirection(dx, dy);
        }
    }

    private void moveEntireSnake() {
        double prevX, prevY, newX, newY, prevDx, prevDy, newDx, newDy;
        prevX = snake.getFirst().getX();
        prevY = snake.getFirst().getY();
        prevDx = snake.getFirst().getDx();
        prevDy = snake.getFirst().getDy();

        snake.getFirst().move();

        for (int i = 1; i < snake.size(); i++) {
            SnakeSegment segment = snake.get(i);
            newX = segment.getX();
            newY = segment.getY();
            newDx = segment.getDx();
            newDy = segment.getDy();
            segment.setX(prevX);
            segment.setY(prevY);
            segment.setDirection(prevDx, prevDy);
            segment.updateRectangle();
            prevX = newX;
            prevY = newY;
            prevDx = newDx;
            prevDy = newDy;
        }
    }


    private void updateSnake(Pane root) {
        moveEntireSnake();

        if (apple.isEatenBySnake(snake.getFirst())) {
            eatTheApple(root);
        }

        if (snakeCollidedWithWall() || snakeCollidedWithSelf()) {
            gameOver();
        }
    }

    private void eatTheApple(Pane root) {
        root.getChildren().remove(apple.getRectangle());
        addApple();
        root.getChildren().add(apple.getRectangle());
        addSegment();
        gameScore++;
        scoreText.setText("SCORE: " + gameScore);
    }

    private boolean snakeCollidedWithWall() {
        return snake.getFirst().getX() >= GRID_SUBDIVISIONS || snake.getFirst().getY() >= GRID_SUBDIVISIONS ||
                snake.getFirst().getX() < 0 || snake.getFirst().getY() < 0;
    }

    private boolean snakeCollidedWithSelf() {
        SnakeSegment head = snake.getFirst();
        if (head.getDx() == 0 && head.getDy() == 0) {
            return false;
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.getX() == snake.get(i).getX() && head.getY() == snake.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    private void addSegment() {
        SnakeSegment last = snake.getLast();
        SnakeSegment newSegment = new SnakeSegment(last.getRectangle().getX(),
                last.getRectangle().getY(), SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
        Image image = new Image(getClass().getResourceAsStream("snake-body.png"));
        newSegment.getRectangle().setFill(new ImagePattern(image));
        snake.add(newSegment);
        ((Pane) snake.getFirst().getRectangle().getParent()).getChildren().add(newSegment.getRectangle());
    }

    private void gameOver() {
        timeline.stop();
        SavedScore savedScore = new SavedScore();
        savedScore.setScore(gameScore);
        savedScore.saveScore();
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
