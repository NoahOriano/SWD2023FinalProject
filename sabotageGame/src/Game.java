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
        Parent root =
                FXMLLoader.load(getClass().getResource("Game.fxml"));
        Scene scene = new Scene(root);      // attach scene graph to scene
        stage.setTitle("Sabotage");
        stage.setScene(scene);              // attach scene to stage
        stage.show();                       // display the stage
    }
}
