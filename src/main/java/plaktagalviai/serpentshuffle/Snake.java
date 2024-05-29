package plaktagalviai.serpentshuffle;

import java.util.ArrayList;
import java.util.List;

public class Snake extends ArrayList<SnakeSegment> {

    /*private ArrayList<SnakeSegment> snake;

    public Snake(ArrayList<SnakeSegment> snake) {
        this.snake = snake;
    }*/

    public SnakeSegment getHead() {
        return this.isEmpty() ? null : this.get(0);
    }

    public SnakeSegment getTail() {
        return this.isEmpty() ? null : this.get(this.size() - 1);
    }

    /*private boolean snakeCollidedWithWall(int GRID_SUBDIVISIONS) {
        return (snake.getFirst().getX() > GRID_SUBDIVISIONS - 1) || (snake.getFirst().getY() > GRID_SUBDIVISIONS - 1) ||
                (snake.getFirst().getX() < 0) || (snake.getFirst().getY() < 0);
    }*/
}
