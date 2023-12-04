import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A class called game that is used as the main driver for play. The driver must be run each time a player
 * wishes to start playing and join a game. Once ran, it will first prompt the user to enter the IP address
 * and port number of the game they either wish to join or host.
 */
public class Game extends Application {// A public class called Game that extends application.
    public static void main(String[] args) {
        launch(args);
    }// Launch the game.

    /** The overridden start() method of the Game class that overrides the generic start() method of java.**/
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("GameJoinScene.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Dawn of Doom");// Set the title of the game to the game name.
        stage.setScene(scene);
        stage.show();// display the stage
    }
}
