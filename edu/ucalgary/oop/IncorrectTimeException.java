/** 
@author ENSF380 Group 20
* IncorrectTimeException is a java class extending the Exception class.
* It is thrown whenever the total time exceeds an allowed time of 60 minutes.
@version    1.2
@since 1.0
*/
package edu.ucalgary.oop;

public class IncorrectTimeException extends Exception{
    public IncorrectTimeException(String message){
        super(message); 
    }
}
