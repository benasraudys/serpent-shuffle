package plaktagalviai.serpentshuffle;
import javafx.scene.shape.Rectangle;
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
        gridX = x/width -8; // TODO fix this, because will not work for different grid sizes
        gridY = y/height -8;
    }
    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isAppleEaten(double snakeX, double snakeY, double recWidth, double recHeight){

        return (gridX == snakeX && gridY == snakeY);


//        if(snakeX  >= x && snakeX  <= x + width && snakeY >= y && snakeY <= y + height){
//            return true;
//        }
//
//        else if(snakeX + recWidth >= x && snakeX + recWidth <= x + width && snakeY + recHeight >= y && snakeY + recHeight <= y + height){
//            return true;
//        }
//        else{
//            return false;
//        }


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

