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


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameApplication extends Application {

    private static final int SCENE_SIZE = 720; // Grid, the snake is moving on, size (in pixels)
    private static final int GRID_SUBDIVISIONS = 16; // To how many parts the grid is divided to
    private static final int SUBDIVISION_LENGTH = SCENE_SIZE / GRID_SUBDIVISIONS;

    private static final int WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 720;


    private final List<SnakeSegment> snake = new ArrayList<>();
    private Apple apple;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);



        // Initialize the snake with one segment at the center
        SnakeSegment initialSegment = new SnakeSegment((float) SCENE_SIZE / 2 - SUBDIVISION_LENGTH / 2f + 15,
                (float)SCENE_SIZE / 2 - SUBDIVISION_LENGTH / 2f + 15,
                SUBDIVISION_LENGTH, SUBDIVISION_LENGTH);
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
        int x = rand.nextInt(SCENE_SIZE / SUBDIVISION_LENGTH) * SUBDIVISION_LENGTH;
        int y = rand.nextInt(SCENE_SIZE / SUBDIVISION_LENGTH) * SUBDIVISION_LENGTH;
        apple = new Apple(x, y, SUBDIVISION_LENGTH, SUBDIVISION_LENGTH);
        Image image = new Image(getClass().getResourceAsStream("apple.png"));
        apple.getRectangle().setFill(new ImagePattern(image));
    }


    private KeyCode lastCode;
    private void updateDirection(KeyCode code) {
        if (snake.isEmpty()) return;
        double dx = 0, dy = 0;
        switch (code) {
            case UP:
                if(lastCode != KeyCode.DOWN){
                dy = -SUBDIVISION_LENGTH;
                }
                break;
            case DOWN:
                if(lastCode != KeyCode.UP) {
                    dy = SUBDIVISION_LENGTH;
                }
                break;
            case LEFT:
                if(lastCode != KeyCode.RIGHT) {
                    dx = -SUBDIVISION_LENGTH;
                }
                break;
            case RIGHT:
                if(lastCode != KeyCode.LEFT) {
                    dx = SUBDIVISION_LENGTH;
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

        // Apple eating
        if (apple.isAppleEaten(snake.getFirst().getX(), snake.getFirst().getY(), SUBDIVISION_LENGTH, SUBDIVISION_LENGTH)){
            root.getChildren().remove(apple.getRectangle());
            addApple();
            root.getChildren().add(apple.getRectangle());
            addSegment();
        }

        snake.getFirst().move();



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
                snake.getFirst().getRectangle().getX() + SUBDIVISION_LENGTH >= SCENE_SIZE ||
                snake.getFirst().getRectangle().getY() <= -60 ||
                snake.getFirst().getRectangle().getY() + SUBDIVISION_LENGTH >= SCENE_SIZE) {
            //game over
            System.out.println("Game over.");
            Platform.exit();
        }

    }

    private void addSegment() {
        SnakeSegment last = snake.getLast();
        SnakeSegment newSegment = new SnakeSegment(last.getRectangle().getX(),
                last.getRectangle().getY(),
                SUBDIVISION_LENGTH, SUBDIVISION_LENGTH);
        Image image = new Image(getClass().getResourceAsStream("icon.png"));
        newSegment.getRectangle().setFill(new ImagePattern(image));
        snake.add(newSegment);
        ((Pane) snake.getFirst().getRectangle().getParent()).getChildren().add(newSegment.getRectangle());
    }


}
