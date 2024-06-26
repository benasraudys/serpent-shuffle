package plaktagalviai.serpentshuffle;

import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class SnakeSegment {
    private final Rectangle rectangle;
    private final int subdivisionLength;
    private double dx;
    private double dy;
    private double x;
    private double y;

    public SnakeSegment(double x, double y, int subdivisionLength, int subdivisionCount) {
        this.rectangle = new Rectangle(x * subdivisionLength, y * subdivisionLength, subdivisionLength, subdivisionLength);
        this.subdivisionLength = subdivisionLength;
        this.x = x;
        this.y = y;
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

    public void updateRectangle() {
        rectangle.setX(subdivisionLength * x);
        rectangle.setY(subdivisionLength * y);
    }

    public void updateCoordinates(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void move() {
        updateCoordinates(dx, dy);
        updateRectangle();
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

    public void setX(double prevX) {
        this.x = prevX;
    }

    public void setY(double prevY) {
        this.y = prevY;
    }
}
