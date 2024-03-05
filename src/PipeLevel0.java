import bagel.*;

import java.util.Random;

public class PipeLevel0 extends Pipe {
    private double gapY;
    private Image topPipe = new Image("res/level/plasticPipe.png");
    private final int highGap=100,midGap=300,lowGap=500;

    private int randomGap;

    public PipeLevel0() {
        super();
        setImage(topPipe);
    }

    @Override
    public double setGapY() {
        Integer[] pipeTypes = {highGap,midGap,lowGap};
        Random random = new Random();
        randomGap = random.nextInt(pipeTypes.length);
        return pipeTypes[randomGap];
    }

}
