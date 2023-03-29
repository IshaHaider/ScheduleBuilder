package ENSF380FinalProject.edu.ucalgary.oop;
import java.sql.*;
import java.util.ArrayList;

public class LoadData {
    private Connection dbConnect;
    private ResultSet results;
    private ArrayList<Animal> animals = new ArrayList<Animal>();
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private ArrayList<Treatment> treatments = new ArrayList<Treatment>();

    public LoadData(){}

    public void createConnection(){     
        try{
            dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/ewr", "root", "Haider1970");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void selectAnimals(){
        try {                    
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM animals");
            while (results.next()){
                Animal newAnimal = new Animal(results.getInt("AnimalID"), results.getString("AnimalNickname"), results.getString("AnimalSpecies"));
                this.animals.add(newAnimal);
                System.out.println("Print results: " + results.getString("AnimalSpecies") + ", " + results.getString("AnimalNickname"));
            }
            myStmt.close();
        } catch (SQLException ex) { ex.printStackTrace(); }
        catch(IllegalArgumentException e) {}
    }    

    public void selectTasks(){
        try {                    
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM tasks");
            while (results.next()){
                Task newTask = new Task(results.getInt("TaskID"), results.getString("Description"), results.getInt("Duration"), results.getInt("MaxWindow"));
                this.tasks.add(newTask);
                System.out.println("Print results: " + results.getInt("TaskID") + ", " + results.getString("Description") + ", " + results.getInt("Duration") + ", " + results.getInt("MaxWindow"));
            }
            myStmt.close();
        } catch (SQLException ex) { ex.printStackTrace(); }
        catch(IllegalArgumentException e) {}
    }  
    
    public void selectTreatments(){
        try {                    
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM treatments");
            while (results.next()){
                Treatment newTreatment = new Treatment(results.getInt("TreatmentID"), results.getInt("AnimalID"), results.getInt("TaskID"), results.getInt("StartHour"));
                this.treatments.add(newTreatment);
                System.out.println("Print results: " + results.getInt("TreatmentID") + ", " + results.getInt("AnimalID") + ", " + results.getInt("TaskID") + ", " + results.getInt("StartHour"));
            }
            myStmt.close();
        } catch (SQLException ex) { ex.printStackTrace(); }
        catch(IllegalArgumentException e) {}
    }  
    
    public void close() {
        try {
            results.close();
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {

        LoadData myJDBC = new LoadData();
 
        myJDBC.createConnection();
        myJDBC.selectAnimals();
        myJDBC.selectTasks();
        myJDBC.selectTreatments();
        System.out.println("Here is a list of array");
        System.out.println(myJDBC.animals.get(14).getID());
        
        myJDBC.close();        
    }
}





