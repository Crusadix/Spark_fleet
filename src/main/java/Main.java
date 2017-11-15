import static spark.Spark.*;


import controllers.BusController;
import services.BusService;

public class Main {
    public static void main(String[] args) {

    	//This block is for testing purposes, creates 4 "test buses"
    	BusService testService = new BusService();
    	new BusController(testService);
    	testService.createBus(1);
    	testService.createBus(2);
    	testService.createBus(3);
    	testService.createBus(4);
    	
    	//VehicleInterface testing = new Ez10(7);
    	
    
    	
    	//new BusController(new BusService());  //comment the controller above and uncomment this  to use only the REST API depicted in BusService.java
    }
}