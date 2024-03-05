import bagel.*;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please filling your name below
 * @author: Yuqi Deng (1138222)
 */
public class ShadowFlap extends AbstractGame {
    private int level,score,frameCount,gameStart,levelUpCount;
    private boolean isLevelUp,Win;
    private final int fontSize =48,scoreMsgOffset =75,shootMsgBelow =68,pipeInterval = 100;
    private final int winScore0 = 10,winScore1 = 30,frameLevelUp =150,weaponInterval=85,flameInterval=20;
    private final Image background0 = new Image("res/level-0/background.png");
    private final Image background1 = new Image("res/level-1/background.png");
    private final Font font = new Font("res/font/slkscr.ttf",fontSize);

    // Massages
    private final String initialMsg = "PRESS SPACE TO START";
    private final String shootMsg = "PRESS 'S' TO SHOOT";
    private final String overMsg = "GAME OVER!";
    private final String winMsg = "CONGRATULATIONS!";
    private final String scoreMsg = "SCORE: ";
    private final String finalScoreMsg = "FINAL SCORE: ";
    private final String levelUpMsg = "LEVEL-UP!";

    // Elements
    // For Level0
    private ArrayList<PipeLevel0> plasticPipes = new ArrayList<>();
    private BirdLevel0 birdLevel0 = new BirdLevel0();
    // For Level1
    private TimescaleControls timescaleControl = new TimescaleControls();
    private ArrayList<PipeLevel1> pipes = new ArrayList<>();
    private BirdLevel1 birdLevel1 = new BirdLevel1();
    private LifeBar lifeBar = new LifeBar();
    private ArrayList<Weapon> weapons = new ArrayList<>();


    public ShadowFlap() {
        isLevelUp=false;
        Win=false;
        score=0;
        gameStart=0;
        level=0;
        frameCount=0;
        levelUpCount=0;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        renderBackgroundScreen(input,level);

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // player haven't pressed SPACE
        if (gameStart ==0) {
            if(level==1){
                font.drawString(shootMsg, Window.getWidth()/2-font.getWidth(shootMsg)/2,
                        (Window.getHeight()/2)+shootMsgBelow);
            }
            font.drawString(initialMsg,Window.getWidth()/2-font.getWidth(initialMsg)/2,Window.getHeight()/2);
        }else{
            frameCount++;
        }

        // Game start
        if (input.wasPressed(Keys.SPACE)) {
            gameStart = 1;
        }

        // Game is going on
        if(!Win && gameStart==1 && lifeBar.getHeartNum()>0){
            if(level == 0 && !isLevelUp) {
                setGame0(input);
            }
            if(level == 1){
                setGame1(input);
            }

        }
        // If bird out of bound, it should be re-spawned at(200,350)
        if(birdOutOfBound(birdLevel0.getY())){
            lifeBar.loseHeart();
            birdLevel0.reSpawned();
        }
        if(birdOutOfBound(birdLevel1.getY())){
            lifeBar.loseHeart();
            birdLevel1.reSpawned();
        }
        // Level up!
        if(isLevelUp&&level==0){
            renderLevelUpScreen();
            levelUpCount++;
            if(levelUpCount == frameLevelUp){
                resetGame();
            }
        }

        // Win
        if(score==winScore1&&level==1){
            Win=true;
            renderWinScreen();
        }

        // Game Over
        if(lifeBar.getHeartNum()==0){renderGameOverScreen();}
    }

    /**
     * Reset the values of the game
     */
    public void resetGame(){
        isLevelUp=false;
        gameStart=0;
        level++;
        timescaleControl.setTimescale(1);
        score=0;
        frameCount=0;
        birdLevel1.reSpawned();
        lifeBar.resetLifeBar();
    }

    /**
     * All Setting about Level 0
     * @param input
     */
    public void setGame0(Input input){
        birdLevel0.birdUpdate(input);
        Rectangle birdBox = birdLevel0.getBox();
        updatePipes0(plasticPipes,birdBox);
        timescaleControl.updateTimeScale(input);
        if(frameCount%pipeInterval==0){
            plasticPipes.add(new PipeLevel0());
        }
        lifeBar.renderHeart(level);
    }

    /**
     * Update information about pipes （Level0）
     * So that the pipeline can be produced continuously
     * @param pipes
     * @param birdBox
     */
    public void updatePipes0(ArrayList<PipeLevel0> pipes, Rectangle birdBox){
        for(PipeLevel0 pipe0: pipes){
            pipe0.pipeUpdate(timescaleControl.getTimescale());
            Rectangle topPipeBox = pipe0.getTopBox();
            Rectangle bottomPipeBox = pipe0.getBottomBox();
            if(collision(birdBox,topPipeBox,bottomPipeBox) && !pipe0.isCollide()){
                lifeBar.loseHeart();
                pipe0.setCollide(true);
            }
            if(updateScore(birdLevel0.getX(),pipe0.getTopBox().right(),pipe0.isPass())){
                pipe0.setPass(true);
            }
        }
    }

    /**
     * All Setting about Level 1
     * @param input
     */
    public void setGame1(Input input){
        birdLevel1.birdUpdate(input);
        Rectangle birdBox = birdLevel1.getBox();
        updatePipes1(pipes,birdBox);
        updateWeapons(birdBox,input);
        timescaleControl.updateTimeScale(input);
        if(frameCount%pipeInterval==0){
            pipes.add(new PipeLevel1());
        }
        if(frameCount>1&&frameCount%weaponInterval==0){
            if (randomWeapon()==0){
                weapons.add(new Bomb());
            }else {
                weapons.add(new Rock());
            }
            Weapon weapon = weapons.get(weapons.size()-1);
            if(overlap(weapon.getWeaponBox(),pipes)){
                weapon.setOverlap(true);
            }
        }
        lifeBar.renderHeart(level);
    }

    /**
     * Randomly generate a Integer
     * @return
     */
    public int randomWeapon(){
        int weapon;
        Random random = new Random();
        weapon = random.nextInt(2);
        return weapon;
    }

    /**
     * Update weapon and detect whether it is picked up by birds
     * @param birdBox
     * @param input
     */
    public void updateWeapons(Rectangle birdBox,Input input){
        for(Weapon weapon:weapons){
            Rectangle weaponBox = weapon.getWeaponBox();
            if(birdBox.intersects(weaponBox)&& weapon.isPickable()
                    &&!weapon.isOverlap() && !birdLevel1.isHoldWeapon()){
                birdLevel1.setHoldWeapon(true);
                weapon.setPickable(false);
            }
            weapon.weaponUpdate(input,birdLevel1,timescaleControl.getTimescale());
            weaponDetectPipe(weapon);
        }
    }

    /**
     * Different target types according to weapon
     * Detect whether weapon is in contact with pipe and attack
     * @param weapon
     */
    public void  weaponDetectPipe(Weapon weapon){
        Rectangle weaponBox = weapon.getWeaponBox();
        for(PipeLevel1 pipe1:pipes){
            Rectangle topPipeBox = pipe1.getTopBox();
            Rectangle bottomPipeBox = pipe1.getBottomBox();
            if(!weapon.isDisappear()&& weapon.isShoot()&& !pipe1.isCollide()){
                // Target Type (Rock) : plastic pipes
                if(weapon.getWeaponImage().equals(weapon.getRockImage())
                        && pipe1.getPipeImage().equals(pipe1.getPlasticTopPipe())
                        && collision(weaponBox,topPipeBox,bottomPipeBox)){
                    if(pipe1.getPipeImage().equals(pipe1.getSteelTopPipe())){
                        pipe1.setCollideFlame(true);
                    }
                    pipe1.setCollide(true);
                    pipe1.setPass(true);
                    weapon.setDisappear(true);
                    score++;
                }
                // Target Type (Bomb) : All pipes
                if((weapon.getWeaponImage().equals(weapon.getBombImage()))
                        && collision(weaponBox,topPipeBox,bottomPipeBox)){
                    if(pipe1.getPipeImage().equals(pipe1.getSteelTopPipe())){
                        pipe1.setCollideFlame(true);
                    }
                    pipe1.setCollide(true);
                    pipe1.setPass(true);
                    weapon.setDisappear(true);
                    score++;
                }
            }
        }
    }

    /**
     * Update information about pipes（Level1）
     * So that the pipeline can be produced continuously
     * @param pipes
     * @param birdBox
     */
    public void updatePipes1(ArrayList<PipeLevel1> pipes, Rectangle birdBox){
        for(PipeLevel1 pipe1: pipes){
            pipe1.pipeUpdate(timescaleControl.getTimescale());
            Rectangle topPipeBox = pipe1.getTopBox();
            Rectangle bottomPipeBox = pipe1.getBottomBox();
            // if bird collided with pipe
            if(collision(birdBox,topPipeBox,bottomPipeBox) && !pipe1.isCollide()){
                if(pipe1.getPipeImage().equals(pipe1.getSteelTopPipe())){
                    pipe1.setCollideFlame(true);
                }
                lifeBar.loseHeart();
                pipe1.setCollide(true);
            }
            // Steel pipe with flame for 20 pixels
            if(pipe1.getPipeImage().equals(pipe1.getSteelTopPipe())){
                 if(!pipe1.isCollideFlame()){
                     pipe1.flamePipeUpdate();
                 }
                 Rectangle topFlameBox = pipe1.getTopFlameBox();
                 Rectangle bottomFlameBox = pipe1.getBottomFlameBox();
                 if(pipe1.getFrameCount()%flameInterval==0){
                     // if bird collided with flame
                     if(collision(birdBox,topFlameBox,bottomFlameBox)&&!pipe1.isCollideFlame()){
                         lifeBar.loseHeart();
                         pipe1.setCollideFlame(true);
                         pipe1.setCollide(true);
                     }
                 }
            }
            if(updateScore(birdLevel0.getX(),pipe1.getTopBox().right(),pipe1.isPass())){

                pipe1.setPass(true);
            }
        }
    }

    /**
     * Whether you get score
     * @param birdX
     * @param pipeX
     * @param pass
     * @return
     */
    public boolean updateScore(double birdX,double pipeX,boolean pass) {
        boolean addScore=false;
        if (birdX > pipeX && !pass) {
            score += 1;
            addScore=true;
        }
        String scoreM = scoreMsg + score;
        font.drawString(scoreM, 100, 100);
        if(level==0 && score == winScore0){
            isLevelUp=true;
        }
        return addScore;
    }

    /**
     * Whether bird out of bound
     * @param birdY
     * @return
     */
    public boolean birdOutOfBound(double birdY) {
        // check for bird out of Bound
        return (birdY > Window.getHeight()) || (birdY< 0);
    }

    /**
     * Detect whether the target box collides with one of the other two boxes
     * @param targetBox
     * @param topBox
     * @param bottomBox
     * @return
     */
    public boolean collision(Rectangle targetBox, Rectangle topBox, Rectangle bottomBox) {
        // check for collision
        return targetBox.intersects(topBox) || targetBox.intersects(bottomBox);
    }
    public void renderGameOverScreen() {
        font.drawString(overMsg,
                (Window.getWidth()/2.0-(font.getWidth(overMsg)/2.0)),
                (Window.getHeight()/2.0-(fontSize/2.0)));
        font.drawString((finalScoreMsg + score),
                (Window.getWidth()/2.0-(font.getWidth(finalScoreMsg + score)/2.0)),
                (Window.getHeight()/2.0-(fontSize/2.0))+scoreMsgOffset);
    }

    /**
     * Detect whether the weapon overlaps with the pipe
     * @param weaponBox
     * @param pipes
     * @return
     */
    public boolean overlap(Rectangle weaponBox,ArrayList<PipeLevel1>pipes){
        for(PipeLevel1 pipe1:pipes){
            Rectangle topPipeBox = pipe1.getTopBox();
            Rectangle bottomPipeBox = pipe1.getBottomBox();
            if(collision(weaponBox,topPipeBox,bottomPipeBox)){
                return true;
            }
        }
        return false;
    }
    //The following two methods are from project1 with some changes
    public void renderBackgroundScreen(Input input,Integer level){
        Image backgroundImage;
        if(level == 0){backgroundImage = background0;}
        else{backgroundImage = background1;}
        backgroundImage.drawFromTopLeft(0, 0);
        // render Intro Massage
        if (gameStart ==0) {
            font.drawString(initialMsg,Window.getWidth()/2-font.getWidth(initialMsg)/2,Window.getHeight()/2);
        }

    }


    public void renderWinScreen(){
        font.drawString(winMsg,
                (Window.getWidth()/2.0-(font.getWidth(winMsg)/2.0)),
                (Window.getHeight()/2.0-(fontSize/2.0)));

        font.drawString(finalScoreMsg+score, Window.getWidth()/2-font.getWidth(finalScoreMsg+score)/2,
                (Window.getHeight()/2)+scoreMsgOffset);

    }
    public void renderLevelUpScreen(){
        font.drawString(levelUpMsg,
                (Window.getWidth()/2.0-(font.getWidth(levelUpMsg)/2.0)),
                (Window.getHeight()/2.0-(fontSize/2.0)));
        font.drawString(finalScoreMsg+score, Window.getWidth()/2-font.getWidth(finalScoreMsg+score)/2,
                (Window.getHeight()/2)+scoreMsgOffset);
    }


}
