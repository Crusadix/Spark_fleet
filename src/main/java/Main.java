import static spark.Spark.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;

import controllers.BusController;
import controllers.BusStopController;
import controllers.PassengerController;
import interfaces.PassengerInterface;
import services.BusService;
import services.BusStopService;
import services.PassengerService;
import utilities.DistanceUtils;
import utilities.FleetManager;

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
		stopTestService.createBusStop("Vuoriharjuntie 19, Espoo");
		stopTestService.createBusStop("Rajamäentie, Espoo");
		stopTestService.createBusStop("Siltakuja, Espoo");
		passengerTestService.createPassenger("Vuoriharjuntie 19, Espoo", "Rajamäentie, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo");
		passengerTestService.createPassenger("Siltakuja, Espoo", "Nöykkiön kirjasto, Espoo"); 
		
		//stopTestService.getStop(1).addPassenger(passengerTestService.getPassengers(1));
		//stopTestService.getStop(2).addPassenger(passengerTestService.getPassengers(2));
		//busTestService.getBus(25).pickPassengers(stopTestService.getStop(4));
		//passengerTestService.moveToBusStop(3, 4);
		busTestService.setRouteWaypoints(1, "Siltakuja 2, Espoo","Rajamäentie, Espoo", "Espoo");
		//busTestService.setRouteWaypoints(2, "Siltakuja 2, Espoo","Rajamäentie, Espoo", "Espoo");

	}

	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
	}
}