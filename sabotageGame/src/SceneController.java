import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SceneController {
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
}
