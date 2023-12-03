import Values.PlayerIdentifier;

public class Evidence {
    private String target;
    PlayerIdentifier identifier;
    private String investigator; // only used in server end;

    public PlayerIdentifier getIdentifier() {
        return identifier;
    }
    public String getInvestigator(){
        return investigator;
    }
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setIdentifier(PlayerIdentifier identifier) {
        this.identifier = identifier;
    }
    public void setInvestigator(String investigator){
        this.investigator=investigator;
    }
    public Evidence(String target, PlayerIdentifier identifier){
        this.target = target;
        this.identifier = identifier;
    }

    /**
     * For server end, investigator never sent over network
     * @param target
     * @param identifier
     */
    public Evidence(String target, PlayerIdentifier identifier, String investigator){
        this.target = target;
        this.identifier = identifier;
        this.investigator = investigator;
    }
}
