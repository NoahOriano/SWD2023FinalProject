// To do list
// - Generate element IDS

import javafx.fxml.FXML;

import javax.swing.*;

/**
 * Contains all different window elements and handling for all three scenes
 * This is done to allow sharing of elements, such as chat, between scenes
 */
public class SceneController {
    @FXML
    JTextArea chatLog;

    @FXML
    JTextField chat;

    @FXML
    JComboBox<String> chatSelector;

    @FXML
    JButton textSubmit;

    @FXML
    JButton hostServerButton;

    @FXML
    JButton joinServerButton;

    @FXML
    JTextField serverPortField;

    @FXML
    JTextField serverIPField;

    // Need to include a way to select and do actions, but that is more complicated
}
