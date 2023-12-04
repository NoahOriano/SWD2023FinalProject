import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.naming.ldap.Control;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class SceneController {

    //Text color: #D6900F
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

    /**SceneMaster*/
    private SceneMaster master;

    public void setMaster(SceneMaster master) {
        this.master = master;
    }

    public SceneMaster getMaster() {
        return master;
    }

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
        controller.master = this.master;
        this.master.setController(controller);
    }

    public void setGameJoinScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameJoinScene.fxml"));
        Parent root = loader.load();
        this.passConnection(loader.getController());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        loadScene(loader, root, stage);
    }
    public void setGameActionScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ActionScene.fxml"));
        Parent root = loader.load();
        this.passConnection(loader.getController());
        SceneControllerForActionScene controller = loader.getController();
        controller.roundsLeft.setText("10");
        controller.chatLog.appendText("Server>>> click join as action and submit To join game");
        controller.statusField.setText("Alive");
        controller.actionOptions.getItems().add("Join");
        controller.playerOptions.getItems().add(this.username);
        controller.usernameField.setText(this.getUsername());
        controller.serverPortField.setText(String.valueOf(this.getPort()));
        controller.serverIPField.setText(this.getIP());
        controller.chatSelector.getItems().add("Global");
        ((SceneControllerForActionScene)loader.getController()).mapPane.setImage(new Image(Str"mapFinal.png"))));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        loadScene(loader, root, stage);

    }
    public void setSignOnScreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignOnScreen.fxml"));
        Parent root = loader.load();
        this.passConnection(loader.getController());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        ((SceneControllerForSignOn)loader.getController()).imageView.setImage(
                new Image(String.valueOf(getClass().getResource("townBackground.png"))));
        ((SceneControllerForSignOn)loader.getController()).serverPortField.setText(String.valueOf(this.port));
        ((SceneControllerForSignOn)loader.getController()).serverIPField.setText(this.IP);
        loadScene(loader, root, stage);
    }

    public void loadScene(FXMLLoader loader, Parent root, Stage stage){
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
