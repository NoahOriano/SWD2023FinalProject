import java.util.ArrayList;
import java.security.SecureRandom;
public class EvidenceFile {
    /**
     * Evidence class that holds the positive and negative values
     */
    /*private class Evidence {
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

     */

    /**
     * ArrayList mainFile stores the individiual elements of Evidence for the suspect of the EvidenceFile that determine
     * his good or bad score
     */
    private ArrayList<Evidence> evidenceList;
    /**
     * The Player who the EvidenceFile is about
     */
    private Player suspect;
    /**
     * The Player who owns this EvidenceFile
     */
    private Player source;
    /**
     * Good counts the total number of evidence that indicate the Player suspect is good
     */
    private int good;
    /**
     *  Bad counts the total number of evidence that indicate the Player suspect is bad
     */
    private int bad;

    /**
     * Getters and Setters Below
     * @return
     */

    public Player getSuspect(){
        return this.suspect;
    }
    public Player getSource(){
        return this.source;
    }

    public ArrayList<Evidence> getMainFile(){
        return this.evidenceList;
    }
    public void setMainFile(ArrayList<Evidence> newMain){
        this.evidenceList = newMain;
    }

    public int getGood(){
        return this.good;
    }
    public int getBad() {
        return this.bad;
    }


    /**
     * addPoints is used to add the good and bad points for the suspect
     * @param morality
     */
    public void addPoints(boolean morality){
        if(morality){
            this.good = this.good+1;
        }else{
            this.bad = this.bad+1;
        }
    }

    /** Initial Creation method
     * Creates an EvidenceFile taking in a Player class to be the suspect of the evidence and a Player Class for the source
     * of evidence to pass down to the evidence that file creates
     * @param newSuspect
     * @param newSource
     */
    public EvidenceFile(Player newSuspect, Player newSource){
        this.suspect = newSuspect;
        this.good = 0;
        this.bad = 0;
        this.evidenceList = new ArrayList<>();
        Evidence start = new Evidence(newSource,newSource.getImposter());
        this.evidenceList.add(start);
        this.source = newSource;
    }

    /**
     * Constructor utilized for createStolenEvidence
     * @param temp
     */
    public EvidenceFile(Player temp){
        this.suspect = temp;
        this.good = 0;
        this.bad = 0;
        this.evidenceList = new ArrayList<>();
        this.source = null;
    }

    /**
     * createEvidence method is for creating new Evidence during Player's investigate method or forge method
     * @param truth
     */
    public void createEvidence(boolean truth){
        Evidence newInfo = new Evidence(this.source, truth);
        this.evidenceList.add(newInfo);
        addPoints(truth);
    }

    /**
     * creates the EvidenceFile that will
     * @return
     */
    public  EvidenceFile createStolenEvidence(){
                EvidenceFile stolen = new EvidenceFile(this.suspect);

                SecureRandom gen = new SecureRandom();
                int randomIndex = gen.nextInt(this.evidenceList.size());

                Evidence temp = new Evidence(this.evidenceList.get(randomIndex).getInspector(),this.evidenceList.get(randomIndex).isDoubleAgent());
                stolen.getMainFile().add(temp);
                        return stolen;
    }

    /**
     * addEvidence takes in an EvidenceFile to add into itself by navigating through itself to ensure that the evidence being
     * added doesn't come from the same source with the same information and also increases the values of good or bad
     * @param file
     */
    public void addEvidenceFile(EvidenceFile file){
        boolean preExists = true;
        for(int x = 0;x< file.getMainFile().size();x++){

            for(int y = 0;y<evidenceList.size();y++){
                if(file.getMainFile().get(x).getInspector().equals(evidenceList.get(y).getInspector()) && file.getMainFile().get(x).isDoubleAgent() == evidenceList.get(y).isDoubleAgent()){
                    preExists = false;
                }
            }
            if(preExists){
                evidenceList.add(file.getMainFile().get(x));
                addPoints(file.getMainFile().get(x).isDoubleAgent());
            }
        }
    }

    /*
    /**
     * addEvidence is used to add individual pieces of evidence and checks to see if they actually get added or not,
     * primarily used for the steal class
     * @param input
     * @return

    public Boolean addEvidence(Evidence input){
        boolean add = true;
        for(int x = 0; x< evidenceList.size();x++){
            if(evidenceList.get(x).getInspector().equals(input.getInspector()) && evidenceList.get(x).isDoubleAgent() == input.isDoubleAgent()){
                add = false;
            }
        }
        if(add){
            evidenceList.add(input);
            addPoints(input.isDoubleAgent());
        }
        return add;
    }

 */


}
