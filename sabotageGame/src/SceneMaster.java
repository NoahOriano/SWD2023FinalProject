import javafx.scene.Parent;
import javafx.stage.Stage;

public class SceneMaster {
    /** Controller the scene master oversees */
    private SceneController controller;

    private GameState gameState;

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

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
