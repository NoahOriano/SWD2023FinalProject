// To do list
// - Generate element IDS

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Contains all different window elements and handling for all three scenes
 * This is done to allow sharing of elements, such as chat, between scenes
 */
public class SceneControllerForActionScene extends SceneController {

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
    ChoiceBox<String> playerOptions;

    @FXML
    ChoiceBox<String> actionOptions;

    @FXML
    TextField usernameField;

    @FXML
    TextField statusField;
    /**
     * Whether the game is over, submitting anything will return user to signon if so
     */
    @FXML
    boolean gameOver;

    /**
     * A variable that controls the image of the map in scene builder.
     **/
    @FXML
    ImageView mapPane;

    /**
     * A variable that controls the user's profile picture.
     **/
    @FXML
    ImageView profPic;

    /**
     * A series of variable that hold the images of the icons on the map.
     **/
    @FXML
    ImageView mapIcon1, mapIcon2, mapIcon3, mapIcon4, mapIcon5,
            mapIcon6, mapIcon7, mapIcon8, mapIcon9, mapIcon10, mapIcon11,
            mapIcon12, mapIcon13, mapIcon14, mapIcon15, mapIcon16;

    /**
     * A private ArrayList of ImageViews that holds the map icons.
     **/
    private ArrayList<ImageView> mapIcons = new ArrayList<>();

    /**
     * The getter for the ArrayList of ImageViews that hold the player icons on the map.
     **/
    public ArrayList<ImageView> getMapIcons() {
        this.mapIcons.add(mapIcon1);
        this.mapIcons.add(mapIcon2);
        this.mapIcons.add(mapIcon3);
        this.mapIcons.add(mapIcon4);
        this.mapIcons.add(mapIcon5);
        this.mapIcons.add(mapIcon6);
        this.mapIcons.add(mapIcon7);
        this.mapIcons.add(mapIcon8);
        for (ImageView imageView : Arrays.asList(mapIcon9, mapIcon10, mapIcon11, mapIcon12, mapIcon13, mapIcon14, mapIcon15, mapIcon16)) {
            this.mapIcons.add(imageView);
        }
        return this.mapIcons;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @FXML
    public void initialize() {
        gameOver = false;
        actionSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!gameOver) {
                    if (actionOptions.getValue().equals("Steal")) {
                        sendMessage(new NetworkMessage(MessageValue.STEAL, playerOptions.getValue(), null, null));
                    } else if (actionOptions.getValue().equals("Pass")) {
                        sendMessage(new NetworkMessage(MessageValue.PASS, playerOptions.getValue(), null, null));
                    } else if (actionOptions.getValue().equals("Forge")) {
                        sendMessage(new NetworkMessage(MessageValue.FORGE, playerOptions.getValue(), null, null));
                    } else if (actionOptions.getValue().equals("Investigate")) {
                        sendMessage(new NetworkMessage(MessageValue.INVESTIGATE, playerOptions.getValue(), null, null));
                    } else if (actionOptions.getValue().equals("Vote")) {
                        sendMessage(new NetworkMessage(MessageValue.VOTE, playerOptions.getValue(), null, null));
                    } else if (actionOptions.getValue().equals("Join")) {
                        sendMessage(new NetworkMessage(MessageValue.JOIN, playerOptions.getValue(), null, null));
                    }
                    else if (actionOptions.getValue().equals("Skip")) {
                        sendMessage(new NetworkMessage(MessageValue.VOTE, null, null, null));
                    }
                } else {
                    try {
                        setSignOnScreen(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        textSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sendMessage(new NetworkMessage(MessageValue.CHAT, chat.getText(), chatSelector.getValue(), null));
            }
        });
        playerOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!gameOver) {
                    if (getMaster().getGameState() != null) {
                        EvidenceList list = getMaster().getGameState().getPlayerFileByName(playerOptions.getValue());
                        String life = "true";
                        if (!list.isLife()) life = "false";
                        evidenceField.setText("For: " + list.getInnocentCount() + ", Against: " + list.getCultistCount() + ", Is Alive: " + life);
                    }
                } else {
                    try {
                        setSignOnScreen(actionEvent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    // Need to include a way to select and do actions, but that is more complicated
}
