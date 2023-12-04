/**
 * Server controller for managing game states and data modifications
 */

import java.util.ArrayList;

public class ServerGameController {
    /**playerStates hold all the different GameStates present within the overall game*/
    public ArrayList<GameState> playerStates;

    /**
     * getIndex methods is a helper method to get the Index of a player in playerStates
     * @param playerID is the input for the identify String of a player
     * @return the Index of a player in playerStates
     */
    public int getIndex(String playerID) {
        int index = -1;
        for (int x = 0; x < playerStates.size(); x++) {
            if (playerStates.get(x).getUsername().equals(playerID)) {
                index = x;
            }
        }
        return index;
    }

    /**
     * getListIndex is a helper method to get the Index of playerID in playerStates.get(playerStatesIndex) playerFiles
     * @param playerID is the input for the String that identifies which player the evidence file is about
     * @param playerStatesIndex is the input for the location of a GameState in playerStates which holds the evidenceFiles
     * @return the index that the playerID can be found at
     */
    public int getListIndex(String playerID, int playerStatesIndex) {
        int listIndex = 0;
        for (int x = 0; x < playerStates.get(playerStatesIndex).getPlayerFiles().size(); x++) {
            if (playerStates.get(playerStatesIndex).getPlayerFiles().get(x).getDefense().equals(playerID)) {
                listIndex = x;
            }
        }
        return listIndex;
    }


    /**
     * Steal method allows thief to steal up to 3 pieces of evidence from a victim
     *
     * @param thief is the input for the user who is stealing the data
     * @param victim is the input for the use who's data is being stolen
     */
    public void steal(String thief, String victim) {
        GameState victimFile = playerStates.get(getIndex(victim));
        GameState thiefFile = playerStates.get(getIndex(thief));
        int counter = 0;

        ArrayList<Evidence> collector = new ArrayList<>();

        for (int i = 0; i < victimFile.getPlayerFiles().size(); i++) {
            for (int j = 0; i < victimFile.getPlayerFiles().get(i).getEvidenceList().size(); j++) {
                collector.add(victimFile.getPlayerFiles().get(i).getEvidenceList().get(j));
            }
        }

        while (counter < 3 && !collector.isEmpty()) {
            int rand = (int) (Math.random() * collector.size());
            Evidence evidence = collector.remove(rand);
            if (thiefFile.getPlayerFiles().get(getListIndex(evidence.getTarget(),
                    getIndex(thief))).addEvidence(evidence.getIdentifier(), evidence.getInvestigator())) {

            }
        }
    }

    /**
     * Allows user to pass all their available evidence on infoAbout to recipient
     * @param user is the input of the user who wants to pass the data to another player
     * @param infoAbout is the input of the player who the evidenceFile is about
     * @param recipient is the input for the player who is recieving the evidenceFile
     */
    public void pass(String user, String infoAbout, String recipient) {
        int userIndex = getIndex(user);
        int recipientIndex = getIndex(recipient);

        int infoIndex = getListIndex(infoAbout, userIndex);
        int infoRecipientIndex = getListIndex(infoAbout, recipientIndex);
        EvidenceList userList = playerStates.get(userIndex).getPlayerFiles().get(infoIndex);

        for (int x = 0; x < playerStates.get(userIndex).getPlayerFiles().size(); x++) {
            PlayerIdentifier inputIdentifier = userList.getEvidenceList().get(x).getIdentifier();
            String inspector = userList.getEvidenceList().get(x).getInvestigator();
            playerStates.get(recipientIndex).getPlayerFiles().get(infoRecipientIndex).addEvidence(inputIdentifier, inspector);
        }
    }

    /**
     * Allows the detective to view the truth about another player or suspect
     * @param detective is the input of the player making the investigative move
     * @param suspect is the input of the player who is being investigated
     */
    public void investigate(String detective, String suspect) {
        int detectiveIndex = getIndex(detective);
        int suspectIndex = getIndex(suspect);

        int detectiveListIndex = getListIndex(suspect, detectiveIndex);

        playerStates.get(detectiveIndex).getPlayerFiles().get(detectiveListIndex).addEvidence(playerStates.get(suspectIndex).getIdentifier(), detective);
    }

    /**
     * forge allows a cultist to create false information about players
     * @param cultist is the input of the cultist who is making the false evidence
     * @param victim is the input of the player who is having false evidence made about them
     */
    public void forge(String cultist, String victim) {
        int cultistIndex = getIndex(cultist);
        int victimIndex = getIndex(victim);

        int cultistListIndex = getListIndex(victim, cultistIndex);

            if (playerStates.get(victimIndex).getIdentifier().equals(PlayerIdentifier.INNOCENT)) {
                playerStates.get(cultistIndex).getPlayerFiles().get(cultistListIndex).addEvidence(PlayerIdentifier.CULTIST, cultist);
            }
            if (playerStates.get(cultistIndex).getIdentifier().equals(PlayerIdentifier.INNOCENT)) {
                playerStates.get(cultistIndex).getPlayerFiles().get(cultistListIndex).addEvidence(PlayerIdentifier.INNOCENT, cultist);
            }
        }

}
