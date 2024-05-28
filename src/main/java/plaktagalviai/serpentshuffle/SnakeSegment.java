package plaktagalviai.serpentshuffle;

import javafx.scene.shape.Rectangle;

public class SnakeSegment {
    private final Rectangle rectangle;
    private final int subdivisionLength;
    private double dx;
    private double dy;
    private double x;
    private double y;

    public SnakeSegment(double x, double y, int subdivisionLength) {
        this.rectangle = new Rectangle(x * subdivisionLength, y * subdivisionLength, subdivisionLength, subdivisionLength);
        this.subdivisionLength = subdivisionLength;
        //this.x = x;
        //this.y = y;
        this.dx = 0;
        this.dy = 0;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setDirection(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
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
}
