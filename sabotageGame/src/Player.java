import java.util.ArrayList;
import java.security.SecureRandom;

/**
 * Player Class is going to contain the action and data of a player inside the game
 */

public class Player {
    private boolean imposter;
    private String profileName;
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
    }

    public int searchPlayerEvidence(Player lost){
        int output = 0;
        for(int x = 0; x< playerFiles.size();x++){
            if(lost == playerFiles.get(x).getSuspect() ){
                output = x;
            }
            else{
                output = -1;
            }
        }
        return output;
    }


    public ArrayList<EvidenceFile> search(Player suspect){
        searchFiles = suspect.getPlayerFiles();
        return searchFiles;
    }

    public void pass(Player ally, Player info){
        int allyIndex = ally.searchPlayerEvidence(info);
        int myIndex = searchPlayerEvidence(info);
        ally.getPlayerFiles().get(allyIndex).addEvidence(getPlayerFiles().get(myIndex));
    }
    public void steal(Player victim){
        SecureRandom gen = new SecureRandom();


    }
    public void forge(){

    }



    }


