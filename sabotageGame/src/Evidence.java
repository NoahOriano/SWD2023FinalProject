public class Evidence {
    private Player inspector;
    boolean culprit;
    private Player target;
    public Player getInspector() {
        return inspector;
    }

    public boolean isDoubleAgent() {
        return culprit;
    }

    public void setInspector(Player inspector) {
        this.inspector = inspector;
    }

    public Evidence(Player newInspector, boolean culprit, Player target){
        this.inspector = newInspector;
        this.culprit = culprit;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public void setCulprit(boolean culprit) {
        this.culprit = culprit;
    }

    public Evidence copy(){
        return new Evidence(this.inspector, this.culprit, this.target);
    }
}
