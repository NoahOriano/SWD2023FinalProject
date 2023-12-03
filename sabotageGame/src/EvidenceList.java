import Values.PlayerIdentifier;

import java.util.ArrayList;

public class EvidenceList {
    private ArrayList<Evidence> evidenceList;
    private int CultistCount;
    private int InnocentCount;

    /**
     * Add the evidence given to the file, used for player end
     * @param identifier
     * @return
     */
    public boolean addEvidence(PlayerIdentifier identifier){

    }

    /**
     * Add the evidence given to the file, used for server end since it must store inspector
     * @param identifier
     * @return
     */
    public boolean addEvidence(PlayerIdentifier identifier, String inspector){

    }

    public Evidence getEvidenceByInsvestigatorName(String name){

    }

    public getEvidenceByIndex(){

    }

    public ArrayList<Evidence> getEvidenceList() {
        return evidenceList;
    }
}
