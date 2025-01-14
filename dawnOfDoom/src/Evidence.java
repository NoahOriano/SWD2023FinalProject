public class Evidence {
    /**
     * target is the person who teh evidence is about
     */
    private String target;
    /**
     * identifier determines if the player is cultist or innocent
     */
    private PlayerIdentifier identifier;
    /**
     * investigator is the person who originally generated the information
     */
    private String investigator; // only used in server end;

    /**
     * Getter and Setters Below
     */
    public PlayerIdentifier getIdentifier() {
        return identifier;
    }

    public String getInvestigator() {
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

    public void setInvestigator(String investigator) {
        this.investigator = investigator;
    }


    /**
     * For client end, to not allow investigator to be sent over
     * @param target is input for the person who the evidence is about
     * @param identifier is the input that determines Cultist or Innocent
     */
    public Evidence(String target, PlayerIdentifier identifier) {
        this.target = target;
        this.identifier = identifier;
    }

    /**
     * For server end, investigator never sent over network
     * @param target is input for the person who the evidence is about
     * @param identifier is the input that determines Cultist or Innocent
     */
    public Evidence(String target, PlayerIdentifier identifier, String investigator) {
        this.target = target;
        this.identifier = identifier;
        this.investigator = investigator;
    }
}
