import java.util.ArrayList;

public class EvidenceList {
    /**evidenceList is an ArrayList of Evidence that stores the evidence which dictates the cultistCount and innocentCount*/
    private ArrayList<Evidence> evidenceList;
    /**String defense is the identifier of the suspect, the one who the cultist count and innocent count is linked to*/
    private String defense;
    /**String prosecutor is the identifier of the owner of this evidence file or the user who's owns this set of information*/
    private String prosecutor;
    /**CultistCount is responsible for storing the amount of evidence that leads to the suspect being a cultist*/
    private int cultistCount;
    /**innocentCount is responsible for storing the amount of evidence that leads to the suspect being innocent*/
    private int innocentCount;
    /**life is responsible for indicating whether the player is alive or dead*/
    private boolean life;



    /**Getters and Setters of The EvidenceList class below*/
    public boolean isLife() {
        return life;
    }
    public void setLife(boolean life) {
        this.life = life;
    }
    public ArrayList<Evidence> getEvidenceList() {
        return evidenceList;
    }

    public int getCultistCount() {
        return cultistCount;
    }
    public int getInnocentCount() {
        return innocentCount;
    }
    public String getDefense() {
        return defense;
    }
    public String getProsecutor() {
        return prosecutor;
    }
    public void setEvidenceList(ArrayList<Evidence> evidenceList) {
        this.evidenceList = evidenceList;
    }
    public void setCultistCount(int cultistCount) {
        this.cultistCount = cultistCount;
    }
    public void setDefense(String defense) {
        this.defense = defense;
    }
    public void setInnocentCount(int innocentCount) {
        this.innocentCount = innocentCount;
    }
    public void setProsecutor(String prosecutor) {
        this.prosecutor = prosecutor;
    }

    /**
     * Constructor for EvidenceList
     * @param defense is the input for the person that the EvidenceList is about
     * @param prosecutor is the input for the person that holds this EvidenceList
     */
    public EvidenceList(String defense, String prosecutor) {
        this.defense = defense;
        this.prosecutor = prosecutor;
        cultistCount = 0;
        innocentCount = 0;
        evidenceList = new ArrayList<>();
        life = true;
    }

    /**
     * Client End Method
     * Add the evidence given to the file, used for player end
     * @param identifier is the input to determine if Cultist or Innocent
     * @return a boolean to notify that the evidence was added
     */
    public boolean addEvidence(PlayerIdentifier identifier) {
        evidenceList.add(new Evidence(defense, identifier));
        if(identifier.equals(PlayerIdentifier.CULTIST)){
            cultistCount++;
        }
        if(identifier.equals(PlayerIdentifier.INNOCENT)){
            innocentCount++;
        }
        return true;
    }

    /**
     * Server End Method
     * Add the evidence given to the file, used for server end since it must store inspector
     * @param identifier is the input to determine if Cultist or Innocent
     * @return a boolean to notify that the evidence was added
     */
    public boolean addEvidence(PlayerIdentifier identifier, String inspector) {
        for (int x = 0; x < evidenceList.size(); x++) {
            //If statement checks to see that only new evidence gets added to the evidenceFile by checking that the source is different
            //and that the information for the evidence is different as well, if both are the same it does not add evidence
            if (evidenceList.get(x).getInvestigator().equals(inspector) && evidenceList.get(x).getIdentifier().equals(identifier)) {
                return false;
            }
        }
        evidenceList.add(new Evidence(this.defense, identifier, this.prosecutor)); //Creates the evidence and adds it to the evidence list
        if(identifier.equals(PlayerIdentifier.CULTIST)){
            cultistCount ++; //If evidence indicated cultist increases the counter of evidence pointing to cultist
        }
        if(identifier.equals(PlayerIdentifier.INNOCENT)){
            innocentCount ++; //If evidence indicated innocent increases the counter of evidence point to innocent
        }
        return true;
    }

    /**
     * Returns a piece of Evidence based on the name of the Investigator, has issues if the Investigator is the
     * cultist due to the fact that they can create contradicting information
     * @param name
     * @return
     */
    public Evidence getEvidenceByInvestigatorName(String name) {
        Evidence evidenceByName = new Evidence(null, null);
        for (int x = 0; x < evidenceList.size(); x++) {
            if (evidenceList.get(x).getInvestigator().equals(name)) {
                evidenceByName = evidenceList.get(x);
            }
        }
        return evidenceByName;
    }
}
