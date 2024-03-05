import bagel.*;


public class BirdLevel1 extends Bird {
    private final Image wingDownL1  = new Image("res/level-1/birdWingDown.png");
    private final Image wingUpL1 = new Image("res/level-1/birdWingUp.png");
    private boolean isHoldWeapon;


    public BirdLevel1() {
        getImage(wingUpL1,wingDownL1);
        isHoldWeapon=false;
    }

    public boolean isHoldWeapon() {
        return isHoldWeapon;
    }

    public void setHoldWeapon(boolean holdWeapon) {isHoldWeapon = holdWeapon;}
}

