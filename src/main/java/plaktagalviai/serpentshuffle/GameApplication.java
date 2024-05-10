package plaktagalviai.serpentshuffle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.List;

public class GameApplication extends Application {

    private static final int RECT_WIDTH = 30;
    private static final int RECT_HEIGHT = 30;
    private static final int MOVE_DISTANCE = 30;

    private static final int SCENE_WIDTH = 720;
    private static final int SCENE_HEIGHT = 720;

    private final List<SnakeSegment> snake = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Initialize the snake with one segment at the center
        SnakeSegment initialSegment = new SnakeSegment(scene.getWidth() / 2 - RECT_WIDTH / 2f + 15,
                scene.getHeight() / 2 - RECT_HEIGHT / 2f + 15,
                RECT_WIDTH, RECT_HEIGHT);
        initialSegment.getRectangle().setFill(Color.GREEN);
        snake.add(initialSegment);
        root.getChildren().add(initialSegment.getRectangle());
        addSegment();
        addSegment();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(90), e -> moveSnake()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        scene.setOnKeyPressed(event -> updateDirection(event.getCode()));

        stage.setTitle("Serpent Shuffle");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void updateDirection(KeyCode code) {
        if (snake.isEmpty()) return;

        double dx = 0, dy = 0;
        switch (code) {
            case UP:
                dy = -MOVE_DISTANCE;
                break;
            case DOWN:
                dy = MOVE_DISTANCE;
                break;
            case LEFT:
                dx = -MOVE_DISTANCE;
                break;
            case RIGHT:
                dx = MOVE_DISTANCE;
                break;
            case SPACE:
                addSegment();
                break;
        }
        if (dx != 0 || dy != 0) {
            snake.getFirst().setDirection(dx, dy);
        }
    }

    private void moveSnake() {
        double prevX, prevY, newX, newY;
        prevX = snake.getFirst().getRectangle().getX();
        prevY = snake.getFirst().getRectangle().getY();
        snake.getFirst().move();

        for (int i = 1; i < snake.size(); i++) {
            newX = snake.get(i).getRectangle().getX();
            newY = snake.get(i).getRectangle().getY();
            snake.get(i).getRectangle().setX(prevX);
            snake.get(i).getRectangle().setY(prevY);
            prevX = newX;
            prevY = newY;
        }
    }

    private void addSegment() {
        SnakeSegment last = snake.getLast();
        SnakeSegment newSegment = new SnakeSegment(last.getRectangle().getX(),
                last.getRectangle().getY(),
                RECT_WIDTH, RECT_HEIGHT);
        newSegment.getRectangle().setFill(Color.GREEN);
        snake.add(newSegment);
        ((Pane) snake.getFirst().getRectangle().getParent()).getChildren().add(newSegment.getRectangle());
    }
}
