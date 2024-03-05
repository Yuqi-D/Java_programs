import bagel.Image;

public class Rock extends Weapon{
    private Image rockImage = new Image("res/level-1/rock.png");
    private final double maxShootRange=25;

    public Rock() {
        setWeaponImage(rockImage);
        setMaxRange(maxShootRange);
    }

    public Image getRockImage() {
        return rockImage;
    }
}
