/** 
 * @author ENSF380 Group 20
 * Species is a java enum representing the five animal species for the EWR database. 
 * The EWR database is permitted to contain only these five animal species
 * @version     1.0
 * @since       1.0
*/

package edu.ucalgary.oop;

public enum Species {
    COYOTE {
        public String toString() { return "coyote"; }
        public String mostActivePeriod() {return "Crepuscular"; }
    },
    FOX {
        public String toString() { return "fox"; }
        public String mostActivePeriod() {return "Nocturnal"; }
    },
    PORCUPINE {
        public String toString() { return "porcupine"; }
        public String mostActivePeriod() {return "Crepuscular"; }
    },
    RACCOON {
        public String toString() { return "raccoon"; }
        public String mostActivePeriod() {return "Nocturnal"; }
    },
    BEAVER {
        public String toString() { return "beaver"; }
        public String mostActivePeriod() {return "Diurnal"; }
    };

    /** toString() method 
     * converts the default enum value's format to a lowercase String
     * @return   String
    */
    public abstract String toString() ;

    public abstract String mostActivePeriod();
}