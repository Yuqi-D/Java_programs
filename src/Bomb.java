import bagel.Image;

public class Bomb extends Weapon{
    private Image bombImage = new Image("res/level-1/bomb.png");
    private final double maxShootRange=50;

    public Bomb() {
        setWeaponImage(bombImage);
        setMaxRange(maxShootRange);
    }

    public Image getBombImage() {
        return bombImage;
    }
}
