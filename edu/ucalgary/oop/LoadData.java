/** 
 * @author ENSF380 Group 20
 * LoadData is a java interface containing the storeHashMap() method used by Animal, 
 * @version     1.2
 * @since       1.0
*/

package edu.ucalgary.oop;

interface LoadData {
    /** storeHashMap()  
     * abstract method to organize SQL data into HashMaps
     * @throws SpeciesNotFoundException
     * @return   void
    */
    void storeHashMap() throws SpeciesNotFoundException;
}
