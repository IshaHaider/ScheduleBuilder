/** 
 * @author ENSF380 Group 20
 * SpeciesNotFoundException is a java class extending the Exception class.
 * It is thrown whenever the animal data entered is associated to a species that this program does not support.
 * @version     1.2
 * @since       1.0
*/

package edu.ucalgary.oop;
public class SpeciesNotFoundException extends Exception{
    public SpeciesNotFoundException(String message){
        super(message); 
    }
}