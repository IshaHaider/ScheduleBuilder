/** 
 * @author ENSF380 Group 20
 * ScheduleFailException is a java class extending the Exception class.
 * It is thrown whenever the schedule is unable to be made
 * @version     1.2
 * @since       1.0
*/

package edu.ucalgary.oop;

public class ScheduleFailException extends Exception{
    public ScheduleFailException(String message){
        super(message); 
    }
}