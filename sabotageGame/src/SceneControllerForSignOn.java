// To do list
// - Generate element IDS

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


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
     * Text field for the password input
     */
    @FXML
    TextField password;

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

    @FXML
    public void initialize(){
        password.setText("IGNORED");
        serverIPField.setText(getIP());
        serverPortField.setText(String.valueOf(getPort()));
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sendMessage(new NetworkMessage(MessageValues.SIGNIN, usernameInput.getText(), null));
            }
        });
    }

    // Need to include a way to select and do actions, but that is more complicated
}
