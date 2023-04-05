/** 
@author ENSF380 Group 20
* Species is a java enum representing the five animal species for the EWR database. 
* The EWR database is permitted to contain only these five animal species
@version    1.0
@since 1.0
*/

package edu.ucalgary.oop;

public enum Species {
    /** toString() method 
    * converts the default enum value formats to lowercase String formats
    * @return   String
    */
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
