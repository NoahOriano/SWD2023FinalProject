import java.util.ArrayList;

public class EvidenceList {
    private ArrayList<Evidence> evidenceList;

    private String defense;
    private String prosecutor;

    private int cultistCount;
    private int innocentCount;



    public int getCultistCount(){
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

    /**Client End Method
     * Add the evidence given to the file, used for player end
     * @param identifier
     * @return
     */
    public boolean addEvidence(PlayerIdentifier identifier){
        evidenceList.add(new Evidence(defense, identifier));
        return true;
        }

    /**Server End Method
     * Add the evidence given to the file, used for server end since it must store inspector
     * @param identifier
     * @return
     */
    public boolean addEvidence(PlayerIdentifier identifier, String inspector){
        boolean added = true;
for(int x = 0; x< evidenceList.size();x++){
    if(evidenceList.get(x).getInvestigator().equals(inspector)&&evidenceList.get(x).getIdentifier().equals(identifier)){
        added = false;
    }
}
if(added){
    evidenceList.add(new Evidence(this.defense,identifier,this.prosecutor));
}
return added;
    }

    public Evidence getEvidenceByInvestigatorName(String name){
        Evidence evidenceByName = new Evidence(null, null);
for(int x = 0; x<evidenceList.size();x++){
    if(evidenceList.get(x).getInvestigator().equals(name)){
        evidenceByName = evidenceList.get(x);
    }
}
return evidenceByName;
    }

            /*

    public getEvidenceByIndex(){

    }
    */

    public ArrayList<Evidence> getEvidenceList() {
        return evidenceList;
    }
}
