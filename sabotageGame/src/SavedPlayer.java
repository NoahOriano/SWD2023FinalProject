/**
 * SavedPlayer class is going to store all the previous player information
 */
public class SavedPlayer {
    private int gameWin;
    private int gameLoss;
    private int imposterCount;
    private int gamesPlayed;
    private double playerRating;
    private int storageIndex;

    
    public void setGameWin(int wins){
        this.gameWin = wins;
    }
    public int getGameWin(){
        return this.gameWin;
    }
    public void setGameLoss(int loss){
        this.gameLoss = loss;
    }
    public int getGameLoss(){
        return this.gameLoss;
    }
    public void setImposterCount(int imposter){
        this.imposterCount=imposter;
    }
    public int getImposterCount(){
        return this.imposterCount;
    }
    public void setGamesPlayed(int games){
        this.gamesPlayed=games;
    }
    public int getGamesPlayed(){
        return this.gamesPlayed;
    }
    public void setPlayerRating(double stars){
        this.playerRating=stars/(Double.valueOf(stars));
    }
    public double getPlayerRating(){
        return this.playerRating;
    }
    public void setStorageIndex(int storage) {
        this.storageIndex = storage;
    }
    public int getStorageIndex(){
        return this.storageIndex;
    }


}
