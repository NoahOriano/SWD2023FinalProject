public class EvidenceFile {
    /**
     * Evidence class that holds the positive and negative values
     */
    private class Evidence {
        private String culprit;
        private boolean doubleAgent;
        private String inspector;

        public String getCulprit() {
            return culprit;
        }

        public String getInspector() {
            return inspector;
        }

        public boolean isDoubleAgent() {
            return doubleAgent;
        }

        public void setCulprit(String culprit) {
            this.culprit = culprit;
        }

        public void setDoubleAgent(boolean doubleAgent) {
            this.doubleAgent = doubleAgent;
        }

        public void setInspector(String inspector) {
            this.inspector = inspector;
        }
    }
}
