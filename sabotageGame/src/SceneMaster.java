import javafx.scene.Parent;
import javafx.stage.Stage;

public class SceneMaster {
    /** Controller the scene master oversees */
    private SceneController controller;
    /** Stage the controller scenes are set on */
    private Stage stage;
    /** Before the stage is set, it must be retrieved later through the parent*/
    private Parent pane;
    /**GameClient runnable capable of handing incoming communications*/
    private ClientMessageRelay client;
    public SceneMaster(SceneController controller, Stage stage){
        this.controller = controller;
        this.stage = stage;
    }

    public SceneMaster(SceneController controller, Parent pane){
        this.controller = controller;
        this.pane = pane;
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

    public void collectStage(){
        if(pane != null){
            stage = (Stage)pane.getScene().getWindow();
        }
    }
}
