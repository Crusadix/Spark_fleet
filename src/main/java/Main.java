import static spark.Spark.*;


import controllers.BusController;
import services.BusService;

public class Main {
    public static void main(String[] args) {

    	port(getHerokuAssignedPort());
    	
    	//This block is for testing purposes, creates 4 "test buses"
    	BusService testService = new BusService();
    	new BusController(testService);
    	testService.createBus(1);
    	testService.createBus(2);
    	testService.createBus(3);
    	testService.createBus(4);
    	
    	//VehicleInterface testing = new Ez10(7);
    	
    
    	
    	//new BusController(new BusService());  //comment the controller block above and uncomment this  to use only the REST API depicted in BusService.java
    }
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}