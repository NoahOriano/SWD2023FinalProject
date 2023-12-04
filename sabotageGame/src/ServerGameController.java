/**
 * Server controller for managing game states and data modifications
 */
import java.util.ArrayList;

/**
 * ServerGameController contains all the main actions that players can make during the game, and stores an
 * ArrayList of all the GameStates
 */
public class ServerGameController {

    public ServerGameController(){
        playerStates = new ArrayList<>();
    }

    /**playerStates hold all the different GameStates present within the overall game*/
    public ArrayList<GameState> playerStates;

    public void addPlayerState(GameState gameState){
        playerStates.add(gameState);
    }

    /**
     * getIndex methods is a helper method to get the Index of a player in playerStates
     * @param playerID is the input for the identify String of a player
     * @return the Index of a player in playerStates
     */
    public int getIndex(String playerID) {
        int index = 0;
        for (int x = 0; x < playerStates.size(); x++) {
            if (playerStates.get(x).getUsername().equals(playerID)) { //Checks to see which index has the same matching userName
                index = x; //Sets the output to that index
            }
        }
        return index; //Returns the index of the playerID in playerStates
    }

    /**
     * getListIndex is a helper method to get the Index of playerID in playerStates.get(playerStatesIndex) playerFiles
     * @param playerID is the input for the String that identifies which player the evidence file is about
     * @param playerStatesIndex is the input for the location of a GameState in playerStates which holds the evidenceFiles
     * @return the index that the playerID can be found at
     */
    public int getListIndex(String playerID, int playerStatesIndex) {
        int listIndex = -1;
        for (int x = 0; x < playerStates.get(playerStatesIndex).getPlayerFiles().size(); x++) {
            if (playerStates.get(playerStatesIndex).getPlayerFiles().get(x).getDefense().equals(playerID)) { //Checks to see which index has the same matching userName
                listIndex = x; //Sets the output to that index
            }
        }
        return listIndex; //Returns the index of the playerID in the playerFiles
    }


    /**
     * Steal method allows thief to steal up to 3 pieces of evidence from a victim, which in reverse works as a pass function
     *
     * @param thief is the input for the user who is stealing the data
     * @param victim is the input for the use who's data is being stolen
     */
    public ArrayList<NetworkMessage> steal(String thief, String victim) {

        GameState victimFile = playerStates.get(getIndex(victim)); //Finds the gameState of the player being stolen
        GameState thiefFile = playerStates.get(getIndex(thief)); //Finds the gameState of the player stealing
        int counter = 0;

        ArrayList<Evidence> collector = new ArrayList<>();

        ArrayList<NetworkMessage> list = new ArrayList<>();

        if(victimFile.getPlayerFiles() != null) {
            for (int i = 0; i < victimFile.getPlayerFiles().size(); i++) {
                for (int j = 0; i < victimFile.getPlayerFiles().get(i).getEvidenceList().size(); j++) {
                    collector.add(victimFile.getPlayerFiles().get(i).getEvidenceList().get(j)); //Adds all the victim's evidence to a new ArrayList of Evidence
                }
            }

            while (counter < 3 && !collector.isEmpty()) {  //Checks to see if 3 pieces of evidence or the collector array are empty
                int rand = (int) (Math.random() * collector.size());
                Evidence evidence = collector.remove(rand); //Deconstructs the collector to get the evidence
                if (thiefFile.getPlayerFiles().get(getListIndex(evidence.getTarget(), //Checks to see if the evidence got added to the evidenceList
                        getIndex(thief))).addEvidence(evidence.getIdentifier(), evidence.getInvestigator())) {
                    String identity = "Innocent";
                    if (evidence.getIdentifier() == PlayerIdentifier.CULTIST) identity = "Cultist";
                    list.add(new NetworkMessage(MessageValue.EVIDENCE, evidence.getTarget(), identity, null));
                    counter++; //Increases to ensure only 3 pieces of evidence get added
                }
            }
        }
        return list;
    }

    /**
     * Allows the detective to view the truth about another player or suspect
     * @param detective is the input of the player making the investigative move
     * @param suspect is the input of the player who is being investigated
     */
    public Evidence investigate(String detective, String suspect) {
        int detectiveIndex = getIndex(detective); //Gets the index of detective for playerStates
        int suspectIndex = getIndex(suspect); //Gets the index of suspect for playerStates

        int detectiveListIndex = getListIndex(suspect, detectiveIndex); //Gets the index of suspect from detectives playerFiles

        //Adds the evidence discovered by the investigation
        if(playerStates.get(detectiveIndex).getPlayerFiles().get(detectiveListIndex).addEvidence(playerStates.get(suspectIndex).getIdentifier(), detective)){
            return playerStates.get(detectiveIndex).getPlayerFiles().get(detectiveListIndex).getEvidenceList().get(
                    playerStates.get(detectiveIndex).getPlayerFiles().get(detectiveListIndex).getEvidenceList().size()-1);
        }
        return null;
    }

    /**
     * forge allows a cultist to create false information about players
     * @param cultist is the input of the cultist who is making the false evidence
     * @param victim is the input of the player who is having false evidence made about them
     */
    public Evidence forge(String cultist, String victim) {
        int cultistIndex = getIndex(cultist); //Gets the index of cultist for playerStates
        int victimIndex = getIndex(victim); //Gets the index of victim for playerStates

        int cultistListIndex = getListIndex(victim, cultistIndex); //Gets the index of victim from cultist playerFiles

            if (playerStates.get(victimIndex).getIdentifier().equals(PlayerIdentifier.INNOCENT)) { //If victim is innocent generates cultist evidence
                //Evidence gets added to the cultist's evidenceFiles
                if(playerStates.get(cultistIndex).getPlayerFiles().get(cultistListIndex).addEvidence(PlayerIdentifier.CULTIST, cultist)){
                    return playerStates.get(cultistIndex).getPlayerFiles().get(cultistListIndex).getEvidenceList().get(
                            playerStates.get(cultistIndex).getPlayerFiles().get(cultistListIndex).getEvidenceList().size()-1);
                };
            }
            else if (playerStates.get(cultistIndex).getIdentifier().equals(PlayerIdentifier.CULTIST)) { //If victim is cultist generate cultist evidence
                //Evidence gets added to the cultist's evidenceFiles
                if(playerStates.get(cultistIndex).getPlayerFiles().get(cultistListIndex).addEvidence(PlayerIdentifier.INNOCENT, cultist)){
                    return playerStates.get(cultistIndex).getPlayerFiles().get(cultistListIndex).getEvidenceList().get(
                            playerStates.get(cultistIndex).getPlayerFiles().get(cultistListIndex).getEvidenceList().size()-1);
                };
            }
            return null;
        }

}
