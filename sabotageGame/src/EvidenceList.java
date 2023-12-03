import java.util.ArrayList;

public class EvidenceList {
    /**
     * evidenceList is an ArrayList of Evidence that stores the evidence which dictates the cultistCount and innocentCount
     */
    private ArrayList<Evidence> evidenceList;
    /**
     * String defense is the identifier of the suspect, the one who the cultist count and innocent count is linked to
     */
    private String defense;
    /**
     * String prosecutor is the identifier of the owner of this evidence file or the user who's owns this set of information
     */
    private String prosecutor;
    /**
     * CultistCount is responsible for storing the amount of evidence that leads to the suspect being a cultist
     */
    private int cultistCount;
    /**
     * innocentCount is responsible for storing the amount of evidence that leads to the suspect being innocent
     */
    private int innocentCount;

    /**
     * Getters and Setters of The EvidenceList class below
     */
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

    public EvidenceList(String defense, String prosecutor) {
        this.defense = defense;
        this.prosecutor = prosecutor;
        cultistCount = 0;
        innocentCount = 0;
        ArrayList<Evidence> evidenceList = new ArrayList<>();
    }

    /**
     * Client End Method
     * Add the evidence given to the file, used for player end
     *
     * @param identifier
     * @return
     */
    public boolean addEvidence(PlayerIdentifier identifier) {
        evidenceList.add(new Evidence(defense, identifier));
        return true;
    }

    /**
     * Server End Method
     * Add the evidence given to the file, used for server end since it must store inspector
     *
     * @param identifier
     * @return
     */
    public boolean addEvidence(PlayerIdentifier identifier, String inspector) {
        boolean added = true;
        for (int x = 0; x < evidenceList.size(); x++) {
            if (evidenceList.get(x).getInvestigator().equals(inspector) && evidenceList.get(x).getIdentifier().equals(identifier)) {
                added = false;
            }
        }
        if (added) {
            evidenceList.add(new Evidence(this.defense, identifier, this.prosecutor));
        }
        return added;
    }


    /**
     * Returns a piece of Evidence based on the name of the Investigator, has issues if the Investigator is the
     * cultist due to the fact that they can create contradicting information
     *
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

            /*

    public int getEvidenceIndex(){

    }
    */


}
