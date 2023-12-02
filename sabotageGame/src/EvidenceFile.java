import java.util.ArrayList;
public class EvidenceFile {
    /**
     * Evidence class that holds the positive and negative values
     */
    private class Evidence {
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

        public Evidence(Player newCulprit, Player newInspector){
            this.inspector = newInspector;
            this.culprit = newCulprit.getImposter();
        }
    }



    /**
     * ArrayList mainFile stores the individiual elements of Evidence for the suspect of the EvidenceFile that determine
     * his good or bad score
     */
    private ArrayList<Evidence> mainFile;
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
        return this.mainFile;
    }
    public void setMainFile(ArrayList<Evidence> newMain){
        this.mainFile = newMain;
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

    /**
     * Creates an EvienceFile taking in a Player class to be the suspect of the evidence and a Player Class for the source
     * of evidence to pass down to the evidence that file creates
     * @param newSuspect
     * @param newSource
     */

    public EvidenceFile(Player newSuspect, Player newSource){
        this.suspect = newSuspect;
        this.good = 0;
        this.bad = 0;
        this.mainFile = new ArrayList<>();
        Evidence start = new Evidence(newSource,newSource);
        this.mainFile.add(start);
        this.source = newSource;
    }


    public Evidence createEvidence(Player defendeant, Player prosecutor){
        Evidence newInfo = new Evidence(defendeant,prosecutor);
        this.mainFile.add(newInfo);
        return newInfo;
    }

    /**
     * addEvidence is used to add individual pieces of evidence and checks to see if they actually get added or not,
     * primarily used for the steal class
     * @param input
     * @return
     */
    public Boolean addEvidence(Evidence input){
        boolean add = true;
        for(int x = 0; x< mainFile.size();x++){
            if(mainFile.get(x).getInspector().equals(input.getInspector()) && mainFile.get(x).isDoubleAgent() == input.isDoubleAgent()){
                add = false;
            }
        }
        if(add){
            mainFile.add(input);
            addPoints(input.isDoubleAgent());
        }
        return add;
    }




    /**
     * addEvidence takes in an EvidenceFile to add into itself by navigating through itself to ensure that the evidence being
     * added doesn't come from the same source with the same information and also increases the values of good or bad
     * @param file
     */
    public void addEvidenceFile(EvidenceFile file){
        boolean preExists = true;
        for(int x = 0;x< file.getMainFile().size();x++){

            for(int y = 0;y<mainFile.size();y++){
                if(file.getMainFile().get(x).getInspector().equals(mainFile.get(y).getInspector()) && file.getMainFile().get(x).isDoubleAgent() == mainFile.get(y).isDoubleAgent()){
                    preExists = false;
                }
            }
            if(preExists){
                mainFile.add(file.getMainFile().get(x));
                addPoints(file.getMainFile().get(x).isDoubleAgent());
            }

        }

    }


}
