import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Bird {

    private Integer frameCount =0,frameSwitch = 10;
    private double x=200 ,initialY=350 ,y;
    private double velocity;
    private static final double flyingY = 6,maxFallingY = 10,gravity=0.4;
    private Rectangle boundingBox;

    private Image imageUp,imageDown;

    public Bird() {
        y = initialY;
        velocity = 0;
    }
    // Refer to project1
    public Rectangle birdUpdate(Input input){
        frameCount += 1;
        if(input.wasPressed(Keys.SPACE)) {
            velocity = -flyingY;
            imageDown.draw(x, y);
        }
        else {
            velocity = Math.min(velocity + gravity, maxFallingY);
            if (frameCount % frameSwitch == 0) {
                imageUp.draw(x, y);
                boundingBox = imageUp.getBoundingBoxAt(new Point(x, y));
            }
            else {
                imageDown.draw(x, y);
                boundingBox = imageDown.getBoundingBoxAt(new Point(x, y));
            }
        }
        y += velocity;
        boundingBox = imageDown.getBoundingBoxAt(new Point(x, y));
        return boundingBox;
    }

    // Setter
    public void reSpawned() {
        this.y = initialY;
    }

    // Getter
    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public Rectangle getBox() {
        return boundingBox;
    }


    public void getImage(Image up, Image down){
        this.imageUp = up;
        this.imageDown = down;
    }


}
