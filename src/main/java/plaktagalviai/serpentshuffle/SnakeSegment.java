package plaktagalviai.serpentshuffle;

import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class SnakeSegment {
    private final Rectangle rectangle;
    private final int subdivisionLength;
    private final int subdivisionCount;
    private double dx;
    private double dy;
    private double x;
    private double y;

    public SnakeSegment(double x, double y, int subdivisionLength, int subdivisionCount) {
        this.rectangle = new Rectangle(x * subdivisionLength, y * subdivisionLength, subdivisionLength, subdivisionLength);
        this.subdivisionLength = subdivisionLength;
        this.subdivisionCount = subdivisionCount;
        this.x = subdivisionCount/2;
        this.y = subdivisionCount/2;
        this.dx = 0;
        this.dy = 0;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setDirection(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        rectangle.setRotate(angle - 90);
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void move() {
        // Updates the rectangle on screen
        rectangle.setX(rectangle.getX() + subdivisionLength * dx);
        rectangle.setY(rectangle.getY() + subdivisionLength * dy);
        // Updates segment coordinates
        x += dx;
        y += dy;
    }

    public void setX(double prevX) {
        this.x = prevX;
    }

    public void setY(double prevY) {
        this.y = prevY;
    }
}
