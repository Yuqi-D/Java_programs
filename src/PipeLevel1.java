import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;


public class PipeLevel1 extends Pipe{
    private  Image pipeImage;
    private final Image flame = new Image("res/level-1/flame.png");
    private final Image steelTopPipe = new Image("res/level-1/steelPipe.png");
    private final Image plasticTopPipe = new Image("res/level/plasticPipe.png");
    private Image[] pipeImages = {steelTopPipe,plasticTopPipe};
    private final double switchFrame = 20,miniY = 100,maxY = 500;
    private int frameCount,flameFrameCount,flameDuration = 30;
    private boolean isCollideFlame;


    public PipeLevel1() {
        frameCount = 0;
        flameFrameCount=0;
        isCollideFlame = false;
        pipeImage= randomImage();
        setImage(pipeImage);
    }

    /**
     * Update flame for steel pipe
     */
    public void flamePipeUpdate(){
        frameCount++;
        if(frameCount%switchFrame==0){
            if(!isCollideFlame && flameFrameCount<flameDuration){
                shootFlame();
                flameFrameCount++;
                frameCount--;
            }
            if(flameFrameCount==flameDuration){
                flameFrameCount=0;
                frameCount+=flameDuration;
            }
        }
    }

    /**
     * Shoot flame from steel pipe
     */
    public void shootFlame(){
        flame.draw(super.getPipeX(),super.getTopPipeY()+Window.getHeight()/2+10);
        flame.draw(super.getPipeX(),super.getBottomPipeY()-Window.getHeight()/2-10,super.getROTATOR());
    }

    @Override
    public double setGapY() {
        double random = new Random().nextDouble();
        return (miniY+(random*(maxY-miniY)));
    }

    /**
     * Randomly generate an image of a pipe
     * @return
     */
    public Image randomImage(){
        int pipeImage;
        Random random = new Random();
        pipeImage = random.nextInt(pipeImages.length);
        return pipeImages[pipeImage];
    }

    // Setter
    public void setCollideFlame(boolean collideFlame) {isCollideFlame = collideFlame;}

    // Getter
    public boolean isCollideFlame() {return isCollideFlame;}

    public int getFrameCount() {return frameCount;}

    public Image getPipeImage() {return pipeImage;}

    public Image getPlasticTopPipe() {return plasticTopPipe;}

    public Image getSteelTopPipe() {return steelTopPipe;}

    public Rectangle getTopFlameBox() {return flame.getBoundingBoxAt(new Point(super.getPipeX(),
            super.getTopPipeY()+Window.getHeight()/2+10));}

    public Rectangle getBottomFlameBox() {return flame.getBoundingBoxAt(new Point(super.getPipeX(),
            super.getBottomPipeY()-Window.getHeight()/2-10));}

}
