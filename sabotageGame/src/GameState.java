import java.util.ArrayList;
/**
 * GameState is going to be the class that holds the details of play state
 */
public class GameState {
    private ArrayList<Player> listOfPlayers;
    private int numPlayers;

    public ArrayList<Player> getListOfPlayers(){
        return this.listOfPlayers;
    }
    public void setListOfPlayers(ArrayList<Player> newListOfPlayers){
        listOfPlayers=newListOfPlayers;
    }
    public int getNumPlayers(){
        return this.numPlayers;
    }
    public void setNumPlayers(int newNum){
        this.numPlayers=newNum;
    }

    public GameState(ArrayList<String> names){
        listOfPlayers = new ArrayList<>();
        for(int x = 0; x<names.size();x++){
            Player client = new Player(names.get(0));
            listOfPlayers.add(client);
        }
        for(int y = 0; y<names.size();y++) {
            listOfPlayers.get(y).createPlayerFiles(listOfPlayers,listOfPlayers.get(y));
        }
        numPlayers = listOfPlayers.size();
    }

    public  int identifyPlayer(Player id){
        int playerID = 0;
        for(int x = 0; x <listOfPlayers.size();x++){
            if(id.equals(listOfPlayers.get(x))){
                                    playerID = x;
            }
        }
        return playerID;
    }


    public void makeMove(Player user, Player victim, int choice){

    }

    }





