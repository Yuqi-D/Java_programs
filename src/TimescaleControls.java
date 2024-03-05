import bagel.*;

public class TimescaleControls{
    private int timescale;
    private final int maxNum=5,miniNum=1;


    public void updateTimeScale(Input input) {
        if(timescale<maxNum && input.wasPressed(Keys.L)){
            timescale+=1;
        }
        if(miniNum<timescale && input.wasPressed(Keys.K)){
            timescale-=1;
        }
    }

    public int getTimescale() {
        return timescale;
    }

    public void setTimescale(int timescale) {this.timescale = timescale;}
}
