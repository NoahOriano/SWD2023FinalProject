import java.util.ArrayList;
import java.security.SecureRandom;

/**
 * Player Class is going to contain the action and data of a player inside the game
 */

public class Player {
    private boolean imposter;
    private String profileName;
    private int playerNumber;
    private ArrayList<EvidenceFile> playerFiles;

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

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Player(String playerID){
        this.profileName = playerID;
    }


    


    }




}
