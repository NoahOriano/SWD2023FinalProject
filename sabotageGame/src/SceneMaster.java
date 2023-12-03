import javafx.stage.Stage;

public class SceneMaster {
    private SceneController controller;

    private Stage stage;
    public SceneMaster(SceneController controller, Stage stage){
        this.controller = controller;
        this.stage = stage;
    }

    public SceneController getController() {
        return controller;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setController(SceneController controller) {
        this.controller = controller;
    }
}
