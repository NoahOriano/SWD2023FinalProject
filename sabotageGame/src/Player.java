import java.util.ArrayList;

/**
 * Player Class is going to contain the action and data of a player inside the game
 */

public class Player {
    private boolean imposter;
    private String profileName;
    private int playerNumber;
    private ArrayList<Evidence> otherPlayers;

    public boolean getImposter(){
        return this.imposter;
    }
    public void setImposter(boolean liar){
        this.imposter = liar;
    }
    public String getProfileName(){
        return this.profileName;
    }
    public void setProfileName(String newPlayer){
        this.profileName = newPlayer;
    }

    public Player(String playerID){
        this.profileName = playerID;
    }

    public int findPlayerIndex(String name){
        int playerIndex = 0;
        for(int x = 0; x< otherPlayers.size();x++){
            if(name.equals(otherPlayers.get(x))){
                playerIndex = x;
            }
            else{
                return -1;
            }
        }
        return playerIndex;
    }

    public void newInformation(String suspect, boolean good){
        findPlayerIndex(suspect);

    }




}
