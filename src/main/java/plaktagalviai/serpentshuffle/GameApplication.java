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


import java.util.List;
import java.util.Random;


import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import javafx.stage.Window;

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

    Timeline timeline;

    @Override
    public void start(Stage stage) {
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
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        scoreText.setX(320);
        scoreText.setY(30);
        root.getChildren().add(scoreText);

        timeline = new Timeline(new KeyFrame(Duration.millis(MOVE_TIME_MILLISECONDS), e -> updateSnake(root)));
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
    }


    private KeyCode lastKeyCode;

    private void updateDirection(KeyCode keyCode) {
        if (snake.isEmpty()) return;

        double dx = 0, dy = 0;

        switch (keyCode) {
            case UP:
                if (lastKeyCode != KeyCode.DOWN) dy = -1;
                break;
            case DOWN:
                if (lastKeyCode != KeyCode.UP) dy = 1;
                break;
            case LEFT:
                if (lastKeyCode != KeyCode.RIGHT) dx = -1;
                break;
            case RIGHT:
                if (lastKeyCode != KeyCode.LEFT) dx = 1;
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

    private void moveEntireSnake(List<SnakeSegment> snake) {
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
            segment.updateRectangle();
            prevX = newX;
            prevY = newY;
            prevDx = newDx;
            prevDy = newDy;

        }
    }


    private void updateSnake(Pane root) {

        moveEntireSnake(snake);

        // Apple eating
        if (apple.isEatenBySnake(snake.getFirst())){
            root.getChildren().remove(apple.getRectangle());
            addApple();
            root.getChildren().add(apple.getRectangle());
            addSegment();
            gameScore++;
            scoreText.setText("SCORE: " + gameScore);
        }

        if (snakeCollidedWithWall(snake)) {
            gameOver();
            return;
        }

        //System.out.println(snake.getFirst().getX() + " " + snake.getFirst().getY());
    }

    private Apple eatTheApple(Apple apple, Pane root) { // TODO make apple hold the root node, so you don't have to pass a reference
        System.out.println("Apple eaten");
        root.getChildren().remove(apple.getRectangle());
        addApple();
        root.getChildren().add(apple.getRectangle());
        addSegment();
        gameScore++;
        scoreText.setText("SCORE: " + gameScore);
        return apple;
    }

    private boolean snakeCollidedWithWall(List<SnakeSegment> snake) {
        return (snake.getFirst().getX() > GRID_SUBDIVISIONS - 1) || (snake.getFirst().getY() > GRID_SUBDIVISIONS - 1) ||
                (snake.getFirst().getX() < 0) || (snake.getFirst().getY() < 0);
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

    private void gameOver(){
        timeline.stop();
        SavedScore savedScore = new SavedScore();
        savedScore.setScore(gameScore);
        savedScore.saveScore();
        Platform.runLater(() -> {
            Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
            if(stage != null){
                try {
                    stage.close();
                    GameOverApplication gameOver = new GameOverApplication();
                    gameOver.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
