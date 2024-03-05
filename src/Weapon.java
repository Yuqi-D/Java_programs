import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Random;

public class Weapon {
    private Image weaponImage;
    private double speed=5,rate = 0.5,weaponX= Window.getWidth(),weaponY,maxRange;
    private boolean pickable=true,isShoot=false,overlap=false,disappear=false;
    private final double miniY = 100,maxY = 500;
    private int movement=0;

    private Image bombImage = new Image("res/level-1/bomb.png");
    private Image rockImage = new Image("res/level-1/rock.png");


    public Weapon() {
        weaponY=setWeaponY();
    }

    public void weaponUpdate(Input input,BirdLevel1 bird,int timescale){
        setSpeed(timescale);
        setMaxRange(maxRange);
        if(pickable){
            weaponX -= speed;
        }else{
            if (bird.isHoldWeapon()&&!isShoot){
                if(input.wasPressed(Keys.S)) {
                    bird.setHoldWeapon(false);
                    this.isShoot = true;
                }else{
                    setWeaponY(bird.getY());
                    setWeaponX(bird.getBox().right());
                }
            }
        }
        renderWeapon(maxRange);
    }

    /**
     * Render Weapon
     * @param maxRange
     */
    public void renderWeapon(double maxRange){
        if(isShoot){
            if(movement<=maxRange){
                this.weaponX+=5;
                movement+=5;
            }else{
                this.disappear=true;
            }
        }
        if(!disappear && !overlap){
            weaponImage.draw(weaponX,weaponY);
        }
    }

    //Check Image
    public Image getRockImage() {return rockImage;}
    public Image getBombImage() {return bombImage;}

    // Getters
    public Image getWeaponImage() {
        return weaponImage;
    }

    public boolean isShoot() {
        return isShoot;
    }

    public boolean isDisappear() {
        return disappear;
    }

    public boolean isPickable() {
        return pickable;
    }

    public boolean isOverlap() {
        return overlap;
    }

    public Rectangle getWeaponBox() {
        return weaponImage.getBoundingBoxAt(new Point(weaponX, weaponY));
    }

    // Setters
    public void setDisappear(boolean disappear) {
        this.disappear = disappear;
    }

    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public void setSpeed(int timeScale){
        speed=5;
        for(int i=1;i<timeScale;i++){
            speed = speed*(1+rate);
        }
    }

    public void setOverlap(boolean overlap) {
        this.overlap = overlap;
    }

    public void setWeaponX(double weaponX) {
        this.weaponX = weaponX;
    }

    public void setWeaponY(double weaponY) {
        this.weaponY = weaponY;
    }

    public double setWeaponY(){
        double random = new Random().nextDouble();
        return (miniY+(random*(maxY-miniY)));
    }
    public void setWeaponImage(Image image){this.weaponImage = image;}
}
