// To do list
// - Generate element IDS

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.control.Button;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
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
        SceneMaster master = new SceneMaster(this);
        setClientRelay(new ClientMessageRelay(master));
        this.setMaster(master);

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
                        getOutput().writeObject(new NetworkMessage(MessageValue.MESSAGE, "Client Connected", null, null));
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
            hostServerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        serverIPField.setText( Inet4Address.getLocalHost().getHostAddress());
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            GameServerStarter.main(new String[]{serverPortField.getText(), String.valueOf(100)});
                        }
                    };
                    thread.start();
                    try {
                        serverIPField.setText( Inet4Address.getLocalHost().getHostAddress());
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

    }

    // Need to include a way to select and do actions, but that is more complicated
}
