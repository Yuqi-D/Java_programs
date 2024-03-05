import bagel.*;

public class LifeBar {
    private Image emptyHeart = new Image("res/level/noLife.png");
    private Image fullHeart = new Image("res/level/fullLife.png");
    private final double xLeft =100,yLeft=15;
    private final int interval =50, maxHeart0=3, maxHeart1=6;
    private double x;
    private int heartNum;

    public LifeBar() {
        heartNum = maxHeart0;
    }

    public void renderHeart(int level){
        int maxHeart=maxHeart0;
        x = xLeft;
        if(level==1) {
            maxHeart=maxHeart1;
        }
        for (int n = 0; n < heartNum; n++) {
            fullHeart.drawFromTopLeft(x, yLeft);
            x += interval;
        }
        if(heartNum != maxHeart){
            for(int e=0;e<(maxHeart-heartNum);e++){
                emptyHeart.drawFromTopLeft(x, yLeft);
                x += interval;
            }
        }
    }

    public void loseHeart(){
        heartNum--;
    }

    public int getHeartNum() {
        return heartNum;
    }

    public void resetLifeBar(){heartNum=maxHeart1;}

}
