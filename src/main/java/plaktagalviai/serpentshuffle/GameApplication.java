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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameApplication extends Application {

    private static final int SCENE_SIZE = 720; // Grid, the snake is moving on, size (in pixels) (has to be divisible by GRID_SUBDIVISIONS)
    private static final int GRID_SUBDIVISIONS = 16; // To how many parts the grid is divided to (has to be divisible by 2)
    private static final int SUBDIVISION_LENGTH = SCENE_SIZE / GRID_SUBDIVISIONS;
    private static final int MOVE_TIME_MILLISECONDS = 140; // How fast the snake moves

    private static final int WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 720;


    private final List<SnakeSegment> snake = new Snake();
    private Apple apple;

    private int gameScore = 0;
    private Text scoreText;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);



        // Initialize the snake with one segment at the center
        int centerCoordinate = (int) (GRID_SUBDIVISIONS / 2f);
        SnakeSegment initialSegment = new SnakeSegment(centerCoordinate,centerCoordinate, SUBDIVISION_LENGTH, GRID_SUBDIVISIONS);
        Image image = new Image(getClass().getResourceAsStream("snake-head.png"));
        initialSegment.getRectangle().setFill(new ImagePattern(image));
        snake.add(initialSegment);
        root.getChildren().add(initialSegment.getRectangle());
        addSegment();
        addSegment();

        addApple();
        root.getChildren().add(apple.getRectangle());

        // Initialize score display
        scoreText = new Text("SCORE: 0");
        scoreText.setFill(Color.BLACK);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scoreText.setX(320);
        scoreText.setY(30);
        root.getChildren().add(scoreText);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(MOVE_TIME_MILLISECONDS), e -> moveSnake(root)));
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
        int x = rand.nextInt(GRID_SUBDIVISIONS) * SUBDIVISION_LENGTH;
        int y = rand.nextInt(GRID_SUBDIVISIONS) * SUBDIVISION_LENGTH;
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
                dy = -1;
                }
                break;
            case DOWN:
                if(lastCode != KeyCode.UP) {
                    dy = 1;
                }
                break;
            case LEFT:
                if(lastCode != KeyCode.RIGHT) {
                    dx = -1;
                }
                break;
            case RIGHT:
                if(lastCode != KeyCode.LEFT) {
                    dx = 1;
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

        double prevX, prevY, newX, newY, prevDx, prevDy, newDx, newDy;
        prevX = snake.getFirst().getX();
        prevY = snake.getFirst().getY();
        prevDx = snake.getFirst().getDx();
        prevDy = snake.getFirst().getDy();

        snake.getFirst().move();

        // Updates each snake segment

        for (int i = 1; i < snake.size(); i++) { // TODO this is really messy, fix this
            SnakeSegment segment = snake.get(i);
            newX = segment.getX();
            newY = segment.getY();
            newDx = segment.getDx();
            newDy = segment.getDy();
            segment.setX(prevX);
            segment.setY(prevY);
            segment.setDirection(prevDx, prevDy);
            segment.getRectangle().setX(prevX * SUBDIVISION_LENGTH);
            segment.getRectangle().setY(prevY * SUBDIVISION_LENGTH);
            prevX = newX;
            prevY = newY;
            prevDx = newDx;
            prevDy = newDy;

        }

        // Apple eating
        if (apple.isAppleEaten(snake.getFirst().getX(), snake.getFirst().getY(), SUBDIVISION_LENGTH, SUBDIVISION_LENGTH)){
            root.getChildren().remove(apple.getRectangle());
            addApple();
            root.getChildren().add(apple.getRectangle());
            addSegment();
            increaseGameScore();
            scoreText.setText("SCORE: " + gameScore);
        }

        //Wall collision

        if ( (snake.getFirst().getX() > GRID_SUBDIVISIONS-1) || (snake.getFirst().getY() > GRID_SUBDIVISIONS-1) ||
             (snake.getFirst().getX() < 0) || (snake.getFirst().getY() < 0) ) { // TODO refactor
            gameOver();
        }

        System.out.println(snake.getFirst().getX() + " " + snake.getFirst().getY());
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

    private void increaseGameScore(){// Game score increases by more as more points are already gained
        if(gameScore <= 5){
            gameScore++;
        }
        else if(gameScore > 5 && gameScore <= 10){
            gameScore+=2;
        }
        else if(gameScore > 10 && gameScore <= 27){
            gameScore+=3;
        }
        else{
            gameScore+=5;
        }
    } // TODO I think this function can be simplified with an exponential equation, but what's wrong with linear increase in the first place?

    private void gameOver() {
        System.out.println("Game over.");
        System.out.println("Game score: " + gameScore);
        Platform.exit();
    }// TODO stop the movement cycle immediately after death


}
