/**
 * Server controller for managing game states and data modifications
 */
import java.util.ArrayList;
import java.security.SecureRandom;

public class ServerGameController {
    public ArrayList<GameState> playerStates;

    public int getIndex(String playerID){
        int index = -1;
        for(int x = 0; x< playerStates.size();x++){
            if(playerStates.get(x).equals(playerID)){
                index = x;
            }
        }
        return index;
    }

    public int getListIndex(String playerID, int playerStatesIndex){
        int listIndex = 0;
        for(int x=0;x<playerStates.get(playerStatesIndex).getPlayerFiles().size();x++ ){
            if(playerStates.get(playerStatesIndex).getPlayerFiles().get(x).getDefense().equals(playerID)){
                listIndex = x;
            }
        }
        return listIndex;
    }


    public void steal(String thief, String victim) {
        int locationThief = getIndex(thief);
        int locationVictim = getIndex(victim);
        boolean added = false;
        int counter = 0;

        SecureRandom gen = new SecureRandom();

        while (!added) {
            int randomNumber = gen.nextInt(playerStates.get(locationVictim).getPlayerFiles().size()); //Index of the random evidenceFile taken from victim
            int evidenceSize = playerStates.get(locationVictim).getPlayerFiles().get(randomNumber).getEvidenceList().size();


            int secondNumber = gen.nextInt(evidenceSize); //Index of the random evidence from the evidence file taken from victim

            String matcher = playerStates.get(locationVictim).getPlayerFiles().get(randomNumber).getDefense();
            int thiefListIndex = getListIndex(matcher, locationThief); //Index of the same random evidenceFile being grabbed from victim
            EvidenceList thiefEvidence = playerStates.get(locationThief).getPlayerFiles().get(thiefListIndex);

            Evidence randomEvidence = playerStates.get(locationVictim).getPlayerFiles().get(randomNumber).getEvidenceList().get(secondNumber);

            boolean check = thiefEvidence.addEvidence(randomEvidence.getIdentifier(), randomEvidence.getInvestigator());
            if (check) {
                counter++;
                if (counter == 3) {
                    added = true;
                }
            }
        }
    }

    public void pass(String user, String infoAbout, String recipient){
int userIndex = getIndex(user);
int recipientIndex = getIndex(recipient);


    }

    public void investigate(String detective, String suspect){

    }

    public void forge(String cultist, String victim){

    }
}
