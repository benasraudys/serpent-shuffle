package plaktagalviai.serpentshuffle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Apple {
    private final Rectangle rectangle;

    private double x; //storing coordinates, because later we can get them
    private double y;
    private double gridX;
    private double gridY;
    private double width;
    private double height;


    public Apple(double x, double y, double width, double height) {
        this.rectangle = new Rectangle(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        gridX = x/width;
        gridY = y/height;
        Image image = new Image(getClass().getResourceAsStream("apple.png"));
        this.getRectangle().setFill(new ImagePattern(image));
    }
    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isEatenBySnake(SnakeSegment snakeHead){
        double snakeX = snakeHead.getX();
        double snakeY = snakeHead.getY();
        return (gridX == snakeX && gridY == snakeY);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public double getGridX() {
        return gridX;
    }

    public double getGridY() {
        return gridY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}

