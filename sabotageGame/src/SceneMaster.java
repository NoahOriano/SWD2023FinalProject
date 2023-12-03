import javafx.scene.Parent;
import javafx.stage.Stage;

public class SceneMaster {
    /** Controller the scene master oversees */
    private SceneController controller;
    public SceneMaster(SceneController controller){
        this.controller = controller;
    }

    public SceneController getController() {
        return controller;
    }

    public void setController(SceneController controller) {
        this.controller = controller;
    }

}
