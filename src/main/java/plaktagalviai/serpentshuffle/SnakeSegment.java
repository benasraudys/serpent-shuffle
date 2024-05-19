package plaktagalviai.serpentshuffle;

import javafx.scene.shape.Rectangle;

public class SnakeSegment {
    private final Rectangle rectangle;
    private double dx;
    private double dy;
    private double x;
    private double y;

    public SnakeSegment(double x, double y, double width, double height) {
        this.rectangle = new Rectangle(x, y, width, height);
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
        rectangle.setX(rectangle.getX() + dx);
        x += dx/45;
        rectangle.setY(rectangle.getY() + dy);
        y += dy/45;
    }
}
