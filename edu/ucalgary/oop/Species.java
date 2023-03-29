package ENSF380FinalProject.edu.ucalgary.oop;

public enum Species {
    COYOTE{
        public String toString() {
            return "coyote";
        }
    },
    FOX{
        public String toString() {
            return "fox";
        }
    },
    PORCUPINE{
        public String toString() {
            return "porcupine";
        }

    },
    RACCOON{
        public String toString() {
            return "raccoon";
        }
    },
    BEAVER{
        public String toString() {
            return "beaver";
        }
    };
    public abstract String toString() ;
}

