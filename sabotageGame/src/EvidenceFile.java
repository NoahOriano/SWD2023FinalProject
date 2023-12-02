import java.util.ArrayList;
public class EvidenceFile {
    /**
     * Evidence class that holds the positive and negative values
     */
    private class Evidence {
        private Player culprit;
        private boolean doubleAgent;
        private Player inspector;

        public Player getCulprit() {
            return culprit;
        }

        public Player getInspector() {
            return inspector;
        }

        public boolean isDoubleAgent() {
            return doubleAgent;
        }

        public void setCulprit(Player culprit) {
            this.culprit = culprit;
        }

        public void setDoubleAgent(boolean doubleAgent) {
            this.doubleAgent = doubleAgent;
        }

        public void setInspector(Player inspector) {
            this.inspector = inspector;
        }
    }

    private ArrayList<Evidence> mainFile;
    private Player suspect;
    private int good;
    private int bad;

    public Player getSuspect(){
        return this.suspect;
    }

    private int getGood(){
        return this.good;
    }
    public int getBad() {
        return this.bad;
    }
    public void addPoints(boolean morality){
        if(morality){
            this.good = this.good+1;
        }else{
            this.bad = this.bad+1;
        }
    }
    public ArrayList<Evidence> grabFile(){
        return this.mainFile;
    }


    public EvidenceFile(Player newSuspect){
        this.suspect = newSuspect;
        this.good = 0;
        this.bad = 0;
        this.mainFile = new ArrayList<>();
    }


    /**
     * addEvidence takes in an EvidenceFile to add into itself by navigating through itself to ensure that the evidence being
     * added doesn't come from the same source with the same information and also increases the values of good or bad
     * @param file
     */
    public void addEvidence(EvidenceFile file){
        boolean preExists = true;
        for(int x = 0;x< file.grabFile().size();x++){

            for(int y = 0;y<mainFile.size();y++){
                if(file.grabFile().get(x).getInspector().equals(mainFile.get(y).getInspector()) && file.grabFile().get(x).isDoubleAgent() == mainFile.get(y).isDoubleAgent()){
                    preExists = false;
                }
            }
            if(preExists){
                mainFile.add(file.grabFile().get(x));
                addPoints(file.grabFile().get(x).isDoubleAgent());
            }

        }

    }


}
