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
    private ArrayList<EvidenceFile> searchFiles;


    /**
     * Getters and Setter Below
     * @return
     */
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

    public ArrayList<EvidenceFile> getPlayerFiles() {
        return playerFiles;
    }
    public void setPlayerFiles(ArrayList<EvidenceFile> playerFiles) {
        this.playerFiles = playerFiles;
    }
    /**
     * Default Constructor
     * @param playerID
     */

    public Player(String playerID, int newPlayerNumber){
        this.profileName = playerID;
        this.playerNumber=newPlayerNumber;
    }

    public void searchPlayerEvidence(Player lost){
        for(int x = 0; x< playerFiles.size();x++){
            
        }
    }


    public void search(Player suspect){

    }
    public void pass(Player ally, Player info){

    }
    public void steal(Player victim){

    }
    public void forge(){

    }



    }


