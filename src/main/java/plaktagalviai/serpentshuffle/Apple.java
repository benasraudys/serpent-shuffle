package plaktagalviai.serpentshuffle;
import javafx.scene.shape.Rectangle;
public class Apple {
    private final Rectangle rectangle;
    public Apple(double x, double y, double width, double height) {
        this.rectangle = new Rectangle(x, y, width, height);
    }
    public Rectangle getRectangle() {
        return rectangle;
    }
}
