// To do list
// - Generate element IDS

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;


/**
 * Contains all different window elements and handling for all three scenes
 * This is done to allow sharing of elements, such as chat, between scenes
 */
public class SceneControllerForSignOn extends SceneController{

    /**
     * Text field for the username input
     */
    @FXML
    TextField usernameInput;

    /**
     * Submit button for the username and password
     */
    @FXML
    Button submit;

    /**
     * Input field for server port when joining a server
     */
    @FXML
    TextField serverPortField;

    /**
     * Input field for server IP when joining a server
     */
    @FXML
    TextField serverIPField;

    /**
     * Whether the user has signed in or not
     */
    boolean signedIn = false;
    @FXML
    public void initialize(){
        serverIPField.setText(getIP());
        serverPortField.setText(String.valueOf(getPort()));
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!signedIn) {
                    sendMessage(new NetworkMessage(MessageValues.SIGNIN, usernameInput.getText(), null, null));
                    setUsername(usernameInput.getText());
                    System.out.println("Sign on attempted");
                }
                else{
                    try {
                        setGameActionScene(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    // Need to include a way to select and do actions, but that is more complicated
}
