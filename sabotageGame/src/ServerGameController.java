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
    /*
    public boolean addRandomEvidence()

     */


    public void steal(String thief, String victim){
        int locationThief = getIndex(thief);
        int locationVictim = getIndex(victim);

        SecureRandom gen = new SecureRandom();

        int randomNumber = gen.nextInt(playerStates.get(locationVictim).getPlayerFiles().size());
        int secondNUmber = gen.nextInt(playerStates.get(locationVictim).getPlayerFiles().get(randomNumber).getEvidenceList().size());

        int thiefEvidenceListIndex = getListIndex(victim,locationVictim);

        boolean checker = false;
        int counter = 0;


        //EvidenceList searched = getEvidenceList(victim)

        }



    public void pass(String user, String infoAbout, String recipient){

    }

    public void investigate(String detective, String suspect){

    }

    public void forge(String cultist, String victim){

    }
}
