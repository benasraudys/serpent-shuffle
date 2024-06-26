package plaktagalviai.serpentshuffle;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.layout.Pane;

public class Snake extends ArrayList<SnakeSegment> {

    public SnakeSegment getHead() {
        return this.isEmpty() ? null : this.get(0);
    }

    public SnakeSegment getTail() {
        return this.isEmpty() ? null : this.get(this.size() - 1);
    }

    public void addSegment(Pane root, int subdivisionLength, int subdivisionCount) {
        SnakeSegment last = this.getTail();
        if (this.getTail() != this.getHead()) {
            Image bodyImage = new Image(getClass().getResourceAsStream("snake-body.png"));
            last.getRectangle().setFill(new ImagePattern(bodyImage));
        }
        SnakeSegment newSegment = new SnakeSegment(last.getRectangle().getX(),
                last.getRectangle().getY(), subdivisionLength, subdivisionCount);
        Image tailImage = new Image(getClass().getResourceAsStream("snake-tail.png"));
        newSegment.getRectangle().setFill(new ImagePattern(tailImage));
        this.add(newSegment);
        root.getChildren().add(newSegment.getRectangle());
        newSegment.getRectangle().toBack();
    }


    public void move() {
        double prevX, prevY, newX, newY, prevDx, prevDy, newDx, newDy;
        prevX = this.getHead().getX();
        prevY = this.getHead().getY();
        prevDx = this.getHead().getDx();
        prevDy = this.getHead().getDy();

        this.getHead().move();

        for (int i = 1; i < this.size(); i++) {
            SnakeSegment segment = this.get(i);
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

    public boolean collidedWithWall(int gridSubdivisions) {
        return this.getHead().getX() >= gridSubdivisions || this.getHead().getY() >= gridSubdivisions ||
                this.getHead().getX() < 0 || this.getHead().getY() < 0;
    }

    public boolean willCollideWithWall(int gridSubdivisions) {
        double toBeX = this.getHead().getX() + this.getHead().getDx();
        double toBeY = this.getHead().getY() + this.getHead().getDy();
        return toBeX >= gridSubdivisions || toBeY >= gridSubdivisions ||
                toBeX < 0 || toBeY < 0;
    }

    public boolean willCollideWithSelf() {
        double toBeX = this.getHead().getX() + this.getHead().getDx();
        double toBeY = this.getHead().getY() + this.getHead().getDy();
        SnakeSegment head = this.getHead();
        if (head.getDx() == 0 && head.getDy() == 0) {
            return false;
        }
        for (int i = 1; i < this.size(); i++) {
            if (toBeX == this.get(i).getX() + this.get(i).getDx() && toBeY == this.get(i).getY() + this.get(i).getDy()) {
                return true;
            }
        }
        return false;
    }

    public boolean collidedWithSelf() {
        SnakeSegment head = this.getHead();
        if (head.getDx() == 0 && head.getDy() == 0) {
            return false;
        }
        for (int i = 1; i < this.size(); i++) {
            if (head.getX() == this.get(i).getX() && head.getY() == this.get(i).getY()) {
                return true;
            }
        }
        return false;
    }
}
