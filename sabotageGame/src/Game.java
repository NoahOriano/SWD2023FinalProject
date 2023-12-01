import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class Game extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("VotingScene.fxml"));
        Scene votingScene = new Scene(root);      // attach scene graph to scene
        root = FXMLLoader.load(getClass().getResource("ActionScene.fxml"));
        Scene actionScene = new Scene(root);      // attach scene graph to scene
        stage.setTitle("Sabotage");
        stage.show();                       // display the stage
    }
}
