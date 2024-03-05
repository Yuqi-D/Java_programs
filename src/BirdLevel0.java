import bagel.*;


public class BirdLevel0 extends Bird {
    private final Image wingDownL0  = new Image("res/level-0/birdWingDown.png");
    private final Image wingUpL0 = new Image("res/level-0/birdWingUp.png");


    public BirdLevel0() {
        super();
        getImage(wingUpL0,wingDownL0);
    }

}
