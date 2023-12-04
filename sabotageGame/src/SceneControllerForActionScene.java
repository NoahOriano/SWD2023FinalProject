// To do list
// - Generate element IDS

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;

import javax.swing.*;


/**
 * Contains all different window elements and handling for all three scenes
 * This is done to allow sharing of elements, such as chat, between scenes
 */
public class SceneControllerForActionScene extends SceneController{

    @FXML
    TextField roundsLeft;
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
    ChoiceBox<String> chatSelector;

    /**
     * Submit button for the chat input
     */
    @FXML
    Button textSubmit;

    /**
     * Text field for displaying port when on a server
     */
    @FXML
    TextField serverPortField;

    /**
     * Text field for displaying IP when on a server
     */
    @FXML
    TextField serverIPField;

    @FXML
    ImageIcon playerIcon;

    @FXML
    TextField evidenceField;

    @FXML
    Button actionSubmit;

    @FXML
    ChoiceBox<String> actionOptions;

    @FXML
    TextField selectedPlayerName;


    /**
     * Whether the game is over, submitting anything will return user to signon if so
     */
    boolean gameOver;

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @FXML
    public void initialize(){
        gameOver = false;
        actionSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(actionOptions.getValue().equals("Steal")){
                    sendMessage(new NetworkMessage(MessageValue.STEAL, selectedPlayerName.getText(), null, null));
                }
                if(actionOptions.getValue().equals("Forge")){
                    sendMessage(new NetworkMessage(MessageValue.FORGE, selectedPlayerName.getText(), null, null));
                }
                if(actionOptions.getValue().equals("Investigate")){
                    sendMessage(new NetworkMessage(MessageValue.INVESTIGATE, selectedPlayerName.getText(), null, null));
                }
                if(actionOptions.getValue().equals("Vote")){
                    sendMessage(new NetworkMessage(MessageValue.VOTE, selectedPlayerName.getText(), null, null));
                }
            }
        });
        textSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sendMessage(new NetworkMessage(MessageValue.CHAT, chat.getText(), chatSelector.getValue(), null));
            }
        });
    }

    // Need to include a way to select and do actions, but that is more complicated
}
