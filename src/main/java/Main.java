import static spark.Spark.*;

import java.io.IOException;
import com.google.maps.errors.ApiException;
import controllers.*;
import services.*;
import utilities.*;

public class Main {

	public static void main(String[] args) throws ApiException, InterruptedException, IOException {

		port(getHerokuAssignedPort());
		
		FleetManager fleetManagement = FleetManager.getInstance();
		BusService busTestService = new BusService();
		BusStopService stopTestService = new BusStopService();
		PassengerService passengerTestService = new PassengerService();
		
		new BusStopController(stopTestService);
		new BusController(busTestService);
		new PassengerController(passengerTestService);

		fleetManagement.addBusService("Espoo", busTestService);
		fleetManagement.addBusStopService("Espoo", stopTestService);
		fleetManagement.addPassengerService("Espoo", passengerTestService);
		
		// This block is for testing purposes
		busTestService.createBus();
		busTestService.createBus();
		busTestService.createBus();
		busTestService.createBus();
		stopTestService.createBusStop("Nöykkiön kirjasto, Espoo");
		stopTestService.createBusStop("Rajamäentie, Espoo");
		stopTestService.createStation("Electric", "Storage hall", "Tuomarila, Espoo");
		stopTestService.createStation("Electric", "Storage hall", "Vuoriharjuntie 19, Espoo");
		
		passengerTestService.createPassenger("Kaurakaski, Espoo", "Vuoriharjuntie 19, Espoo");
		passengerTestService.createPassenger("Rajamäentie, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Kedonkukka, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Sunantie, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Niittytöyry, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Riimuniityntie, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Kantokaski, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Lehtikaskentie, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Kedonkukka, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Kedonkukka, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Kedonkukka, Espoo");
		passengerTestService.createPassenger("Tuomarila, Espoo", "Rajamäentie, Espoo");
		passengerTestService.createPassenger("Vuoriharjuntie 19, Espoo", "Tuomarila, Espoo");
		passengerTestService.createPassenger("Vuoriharjuntie 19, Espoo", "Rajamäentie, Espoo");
		busTestService.getBus(1).pickPassenger(passengerTestService.getPassenger(3));
		busTestService.getBus(1).pickPassenger(passengerTestService.getPassenger(4));
		busTestService.getBus(1).pickPassenger(passengerTestService.getPassenger(5));
		busTestService.getBus(1).pickPassenger(passengerTestService.getPassenger(6));
		busTestService.getBus(1).pickPassenger(passengerTestService.getPassenger(7));
		busTestService.getBus(1).pickPassenger(passengerTestService.getPassenger(8));
		busTestService.getBus(1).pickPassenger(passengerTestService.getPassenger(9));

		
		/* //stopTestService.getStop(1).addPassenger(passengerTestService.getPassengers(1));
		//stopTestService.getStop(2).addPassenger(passengerTestService.getPassengers(2));
		
		//passengerTestService.moveToBusStop(3, 4);
		//busTestService.setRouteWaypoints(1, "Siltakuja 2, Espoo","Rajamäentie, Espoo", "Espoo");
		//busTestService.dri,,veCurrentRoute(1);
		//busTestService.setRouteWaypoints(2, "Siltakuja 2, Espoo","Rajamäentie, Espoo", "Espoo");
	*/
	
	}

	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
	}
}