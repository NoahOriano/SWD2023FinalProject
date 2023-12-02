import java.util.ArrayList;
import java.security.SecureRandom;

/**
 * Player Class is going to contain the action and data of a player inside the game
 */

public class Player {
    /**Boolean to determine traitor*/
    private boolean imposter;
    /**String profileName for an identifier of Player*/
    private String profileName;
    /**playerFiles is an ArrayList of EvidenceFile, each EvidenceFile a player has that's in the array, is linked to a player*/
    private ArrayList<EvidenceFile> playerFiles;
    /**
     * searchFiles is used to grab anotherplayer's playerFiles and allow them to be displayed for another player
     * Well it could be I'm not sure on the implementation of how we should display search
     */
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

    public Player(String playerID){
        this.profileName = playerID;
        this.playerFiles = new ArrayList<>();
    }

    public void createPlayerFiles(ArrayList<Player> opponents, Player self){
        for(int x =0; x< opponents.size();x++){
            this.playerFiles.add(new EvidenceFile(opponents.get(x),self));
        }
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
        ally.getPlayerFiles().get(allyIndex).addEvidenceFile(getPlayerFiles().get(myIndex));
    }

    /**
     * steal method allows the player to take 3 random piecies of individual evidence from the victim and add it to their
     * own files
     * @param victim
     */
    public void steal(Player victim){
        SecureRandom gen = new SecureRandom();
int x = 0;
while(x<3){
    int randomFile = gen.nextInt(victim.getPlayerFiles().size());
    int randomNum = gen.nextInt(victim.getPlayerFiles().get(randomFile).getMainFile().size());
    int randomIndex = searchPlayerEvidence(victim.getPlayerFiles().get(randomFile).getSuspect());
    boolean added = playerFiles.get(randomIndex).addEvidence(victim.getPlayerFiles().get(randomFile).getMainFile().get(randomNum));
    if(added){
        x++;
    }
}


    }
    public void forge(){

    }



    }


