public class SceneMaster {
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
