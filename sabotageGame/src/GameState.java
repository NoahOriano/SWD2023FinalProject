import java.util.ArrayList;

public class GameState {
    /**
     * String Identifier for the GameState class
     */
    private String username;

    PlayerIdentifier identifier;
    private ArrayList<EvidenceList> playerFiles; // will contain file on yourself



    /**
     * Getters and Setters of the GameState Class
     */
    public PlayerIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(PlayerIdentifier identifier) {
        this.identifier = identifier;
    }
    public String getUsername() {
        return username;
    }
    public ArrayList<EvidenceList> getPlayerFiles() {
        return playerFiles;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPlayerFiles(ArrayList<EvidenceList> playerFiles) {
        this.playerFiles = playerFiles;
    }

    /**
     * Constructor for the GameState class, taking in the username of the player
     * @param username
     */
    public GameState(String username){
        this.username = username;
        playerFiles = new ArrayList<>();
    }

    /**
     * getPlayerFileByName is a method that allows to grab an EvidenceFile about the input name
     * @param name input of the suspect/defense
     * @return the evidence file
     */
    private EvidenceList getPlayerFileByName(String name){
        EvidenceList output = new EvidenceList(null,null);
        for(int x =0;x< playerFiles.size();x++){
            if(playerFiles.get(x).getDefense().equals(name)){
                output = playerFiles.get(x);
            }
        }
        return output;
    }

    /**
     * Generates a new EvidenceList for a newly joined player
     * @param newUser username of player who has joined lobby
*/
    public void addPlayerFile(String newUser){
        playerFiles.add(new EvidenceList(newUser,username));
    }

}
