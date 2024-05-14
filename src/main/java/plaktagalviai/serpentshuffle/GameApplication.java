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


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameApplication extends Application {

    private static final int RECT_WIDTH = 30;
    private static final int RECT_HEIGHT = 30;
    private static final int MOVE_DISTANCE = 30;

    private static final int SCENE_WIDTH = 1280;
    private static final int SCENE_HEIGHT = 720;


    private final List<SnakeSegment> snake = new ArrayList<>();
    private Apple apple;

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

        addApple();
        root.getChildren().add(apple.getRectangle());


        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(90), e -> moveSnake(root)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        scene.setOnKeyPressed(event -> updateDirection(event.getCode()));

        stage.setTitle("Serpent Shuffle");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void addApple() {
        Random rand = new Random();
        int x = rand.nextInt(SCENE_WIDTH / RECT_WIDTH);
        x = x * RECT_WIDTH;
        int y = rand.nextInt(SCENE_HEIGHT / RECT_HEIGHT);
        y = y * RECT_HEIGHT;
        apple = new Apple(x , y , RECT_WIDTH, RECT_HEIGHT);
        apple.getRectangle().setFill(Color.RED);
    }

    private KeyCode lastCode;
    private void updateDirection(KeyCode code) {
        if (snake.isEmpty()) return;
        double dx = 0, dy = 0;
        switch (code) {
            case UP:
                if(lastCode != KeyCode.DOWN){
                dy = -MOVE_DISTANCE;
                }
                break;
            case DOWN:
                if(lastCode != KeyCode.UP) {
                    dy = MOVE_DISTANCE;
                }
                break;
            case LEFT:
                if(lastCode != KeyCode.RIGHT) {
                    dx = -MOVE_DISTANCE;
                }
                break;
            case RIGHT:
                if(lastCode != KeyCode.LEFT) {
                    dx = MOVE_DISTANCE;
                }
                break;
            case SPACE:
                addSegment();
                break;
        }
        lastCode = code;
        if (dx != 0 || dy != 0) {
            snake.getFirst().setDirection(dx, dy);
        }
    }

    private void moveSnake(Pane root) {
        double prevX, prevY, newX, newY;
        prevX = snake.getFirst().getRectangle().getX();
        prevY = snake.getFirst().getRectangle().getY();
        snake.getFirst().move();

        if (apple.isAppleEaten(prevX, prevY, RECT_WIDTH, RECT_HEIGHT)){//checks if snake is on apple
            root.getChildren().remove(apple.getRectangle());//removes old apple and generates new one
            addApple();
            root.getChildren().add(apple.getRectangle());
            addSegment();
        }



        for (int i = 1; i < snake.size(); i++) {
            newX = snake.get(i).getRectangle().getX();
            newY = snake.get(i).getRectangle().getY();
            snake.get(i).getRectangle().setX(prevX);
            snake.get(i).getRectangle().setY(prevY);
            prevX = newX;
            prevY = newY;

        }

        //Wall collision
        if (snake.getFirst().getRectangle().getX() <= -60 ||
                snake.getFirst().getRectangle().getX() + RECT_WIDTH >= SCENE_WIDTH ||
                snake.getFirst().getRectangle().getY() <= -60 ||
                snake.getFirst().getRectangle().getY() + RECT_HEIGHT >= SCENE_HEIGHT) {
            //gameover
            System.out.println("Game over.");
            Platform.exit();
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
