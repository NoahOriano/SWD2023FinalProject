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
public class SceneControllerForActionScene extends SceneController{
    /**
     * Text area for the chat log
     */
    @FXML
    TextArea chatLog;

    /**
     * Text field for the chat input
     */
    @FXML
    TextField chat;

    /**
     * Selector for the chat recipients
     */
    @FXML
    ComboBox<String> chatSelector;

    /**
     * Submit button for the chat input
     */
    @FXML
    Button textSubmit;

    /**
     * Input field for displaying port when on a server
     */
    @FXML
    TextField serverPortField;

    /**
     * Input field for displaying IP when on a server
     */
    @FXML
    TextField serverIPField;

    @FXML
    public void initialize(){

    }

    // Need to include a way to select and do actions, but that is more complicated
}
