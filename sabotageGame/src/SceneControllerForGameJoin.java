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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Contains all different window elements and handling for all three scenes
 * This is done to allow sharing of elements, such as chat, between scenes
 */
public class SceneControllerForGameJoin extends SceneController{
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
    @FXML
    public void initialize(){
        setClientRelay(new ClientMessageRelay(new SceneMaster(this)));
        setService(Executors.newFixedThreadPool(3));

            joinServerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        String IP = serverIPField.getText();
                        int port = Integer.parseInt(serverPortField.getText());
                        setConnection( new Socket(IP, port));
                        setOutput( new ObjectOutputStream(getConnection().getOutputStream()));
                        setInput( new ObjectInputStream(getConnection().getInputStream()));
                        getOutput().writeObject(new NetworkMessage(MessageValues.MESSAGE, "Client Connected", null));
                        getOutput().flush();
                        // Connection was successful so save IP and Port info
                        setPort(port);
                        setIP(IP);
                        getService().execute(getClientRelay());
                        getClientRelay().connect(getConnection(), getOutput(), getInput());
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

    // Need to include a way to select and do actions, but that is more complicated
}
