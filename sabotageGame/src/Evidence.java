public class Evidence {
    private Player inspector;
    boolean culprit;
    public Player getInspector() {
        return inspector;
    }

    public boolean isDoubleAgent() {
        return culprit;
    }

    public void setInspector(Player inspector) {
        this.inspector = inspector;
    }

    public Evidence(Player newInspector, boolean forged){
        this.inspector = newInspector;
        this.culprit = forged;
    }
}
