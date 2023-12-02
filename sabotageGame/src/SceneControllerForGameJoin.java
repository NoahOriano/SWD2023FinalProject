// To do list
// - Generate element IDS

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.control.Button;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * Contains all different window elements and handling for all three scenes
 * This is done to allow sharing of elements, such as chat, between scenes
 */
public class SceneControllerForGameJoin {
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
     * Host server button for game start scene
     */
    @FXML
    Button hostServerButton;

    /**
     * Join server button for game start scene
     */
    @FXML
    Button joinServerButton;

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
     * Connection to the server
     */
    private Socket connection;
    /**
     * Output stream for messages over the network
     */
    private ObjectOutputStream output;
    /**
     * Input stream for messages over the network
     */
    private ObjectInputStream input;
    @FXML
    public void initialize(){
        if(joinServerButton != null) {
            joinServerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        connection = new Socket(serverIPField.getText(), Integer.parseInt(serverPortField.getText()));
                        output = new ObjectOutputStream(connection.getOutputStream());
                        input = new ObjectInputStream(connection.getInputStream());
                        output.writeObject(new NetworkMessage(MessageValues.MESSAGE, "Client Connected", null));
                        output.flush();
                        try {
                            setSignOnScreen(actionEvent);
                        } catch (IOException e) {
                            System.out.println("Connected, but failed to switch screens");
                        }
                    } catch (IOException e) {
                        System.out.println("NOT CONNECTED");
                    }
                }
            });
        }
    }

    public void setGameJoinScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GameJoinScene.fxml"));
        Scene scene = new Scene(root);      // attach scene graph to scene
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);              // attach scene to stage
        stage.show();                       // display the stage
    }
    public void setGameActionScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ActionScene.fxml"));
        Scene scene = new Scene(root);      // attach scene graph to scene
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);              // attach scene to stage
        stage.show();                       // display the stage
    }
    public void setSignOnScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignOnScreen.fxml"));
        loader.setController(this);
        Scene scene = new Scene(loader.load());      // attach scene graph to scene
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);              // attach scene to stage
        stage.show();                       // display the stage
    }

    // Need to include a way to select and do actions, but that is more complicated
}
