package plaktagalviai.serpentshuffle;

import java.util.ArrayList;

public class Snake extends ArrayList<SnakeSegment> {

    public SnakeSegment getHead() {
        return this.isEmpty() ? null : this.get(0);
    }

    public SnakeSegment getTail() {
        return this.isEmpty() ? null : this.get(this.size() - 1);
    }
}
