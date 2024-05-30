package plaktagalviai.serpentshuffle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Apple {
    private final Rectangle rectangle;

    private final double x;
    private final double y;


    public Apple(double x, double y, double subdivisionLength) {
        this.x = x;
        this.y = y;
        double sceneX = x * subdivisionLength;
        double sceneY = y * subdivisionLength;
        this.rectangle = new Rectangle(sceneX, sceneY, subdivisionLength, subdivisionLength);
        Image image = new Image(getClass().getResourceAsStream("apple.png"));
        this.getRectangle().setFill(new ImagePattern(image));
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isEatenBySnake(SnakeSegment snakeHead){
        double snakeX = snakeHead.getX();
        double snakeY = snakeHead.getY();
        return (x == snakeX && y == snakeY);
    }
}

