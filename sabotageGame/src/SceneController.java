import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SceneController {
    /**
     * GridPane used by all scenes, able to retrieve stage through it
     */
    @FXML
    GridPane pane;
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
    /**
     * Port client is connected to
     */
    private int port;

    /**
     * IP address client is connected to
     */
    private String IP;

    /**Username of the player*/
    private String username;

    /**GameClient runnable capable of handing incoming communications*/
    private ClientMessageRelay client;

    /**Executor service for running services*/
    private ExecutorService service;

    public ExecutorService getService() {
        return service;
    }

    public void setService(ExecutorService service) {
        this.service = service;
    }

    public ClientMessageRelay getClientRelay() {
        return client;
    }

    public void setClientRelay(ClientMessageRelay client) {
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getConnection() {
        return connection;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void setConnection(Socket connection) {
        this.connection = connection;
    }

    public void setInput(ObjectInputStream input) {
        this.input = input;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    private void passConnection(SceneController controller){
        controller.connection = this.connection;
        controller.input = this.input;
        controller.output = this.output;
        controller.client = this.client;
        controller.IP = this.IP;
        controller.port = this.port;
        controller.service = this.service;
        controller.username = this.username;
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
        Scene scene = new Scene(loader.load());      // attach scene graph to scene
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);              // attach scene to stage
        stage.show();                       // display the stage
    }
    public void setGameJoinScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GameJoinScene.fxml"));
        Scene scene = new Scene(root);      // attach scene graph to scene
        stage.setScene(scene);              // attach scene to stage
        stage.show();                       // display the stage
    }
    public void setSignOnScreen(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignOnScreen.fxml"));
        Scene scene = new Scene(loader.load());      // attach scene graph to scene
        stage.setScene(scene);              // attach scene to stage
        stage.show();                       // display the stage
    }
    public void setGameActionScene(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ActionScene.fxml"));
        Scene scene = new Scene(root);      // attach scene graph to scene
        stage.setScene(scene);              // attach scene to stage
        stage.show();                       // display the stage
    }

    public void sendMessage(NetworkMessage message){
        try {
            output.writeObject(message);
            output.flush();
        }
        catch (IOException e){
            // Unsure how to handle exceptions here, don't throw them!! Usually player can just resubmit
        }

    }
}
