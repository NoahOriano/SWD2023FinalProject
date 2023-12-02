/**
 * Evidence class that holds the positive and negative values
 */
public class Evidence {
    private String culprit;
    private boolean doubleAgent;
    private int goodGuy;
    private int badGuy;

    public int getGoodGuy(){
        return this.goodGuy;
    }
    public int getBadGuy(){
        return this.badGuy;
    }
    public String getCulprit(){
        return this.culprit;
    }
    public void defend(){
        goodGuy ++;
    }
    public void accuse(){
        badGuy ++;
    }

    public void equals(String ){

    }

}
