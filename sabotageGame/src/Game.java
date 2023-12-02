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
        root = FXMLLoader.load(getClass().getResource("GameJoinScene.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Sabotage");
        stage.setScene(scene);
        stage.show();                       // display the stage
    }
}
