import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Pipe {
    private Image pipeTopImage;
    private DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);

    private double pipeX = Window.getWidth();
    private double topPipeY,bottomPipeY,speed,randomGap;
    private final double initialSpeed=5;
    private final int  gap = 168;
    private final double rate = 0.5;

    private boolean isCollide, isPass;

    public Pipe() {
        speed = initialSpeed;
        isCollide = false;
        isPass = false;
        randomGap = setGapY();
    }

    /**
     * Update the details about pipes
     * @param timescale
     */
    public void pipeUpdate (int timescale){
        topPipeY = randomGap-pipeTopImage.getHeight()/2;
        bottomPipeY = randomGap+pipeTopImage.getHeight()/2+gap;

        setSpeed(timescale);

        if(!isCollide){renderPipeSet();}
    }

    /**
     * Render images of top pipe and bottom pipe
     */
    public void renderPipeSet() {
        pipeTopImage.draw(pipeX, topPipeY);
        pipeTopImage.draw(pipeX, bottomPipeY, ROTATOR);
    }

    //Setter
    public void setPass(boolean pass) {
        isPass = pass;
    }
    public void setCollide(boolean collide) {
        this.isCollide = collide;
    }
    public void setImage(Image pipeTopImage){this.pipeTopImage = pipeTopImage;}

    public abstract double setGapY();

    public void setSpeed(int timeScale){
        speed=5;
        for(int i=1;i<timeScale;i++){
            speed = speed*(1+rate);
        }
        pipeX -= speed;
    }

    // Getter
    public boolean isCollide() {return isCollide;}

    public boolean isPass() {return isPass;}

    public Rectangle getTopBox() {return pipeTopImage.getBoundingBoxAt(new Point(pipeX, topPipeY));}

    public Rectangle getBottomBox() {return pipeTopImage.getBoundingBoxAt(new Point(pipeX, bottomPipeY));}

    public double getPipeX() {return pipeX;}

    public double getTopPipeY() {return topPipeY;}

    public double getBottomPipeY() {return bottomPipeY;}

    public DrawOptions getROTATOR() {return ROTATOR;}



}
