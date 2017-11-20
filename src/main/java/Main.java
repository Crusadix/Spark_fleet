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
import services.BusService;
import services.BusStopService;
import services.PassengerService;

public class Main {

	public static void main(String[] args) throws ApiException, InterruptedException, IOException {

		port(getHerokuAssignedPort());

		// This block is for testing purposes
		BusService busTestService = new BusService();
		new BusController(busTestService);
		busTestService.createBus(1);
		busTestService.createBus(2);
		busTestService.createBus(3);
		busTestService.createBus(4);
		BusStopService stopTestService = new BusStopService();
		new BusStopController(stopTestService);
		//stopTestService.createStation(1, "Electric", "Warehouse", "Sello, Espoo");
		stopTestService.createBusStop(1, "Nöykkiön kirjasto, Espoo");
		stopTestService.createBusStop(2, "Vuoriharjuntie 19, Espoo");

		PassengerService passengerService = new PassengerService();
		new PassengerController(passengerService);

		passengerService.createPassenger(1, "Siltakuja 2, Espoo", "Kauniainen, Espoo");
		passengerService.createPassenger(2, "Siltakuja 2, Espoo", "Kauniainen, Espoo");

		stopTestService.getStop(2).addPassenger(passengerService.getPassengers(1));
		stopTestService.getStop(2).addPassenger(passengerService.getPassengers(2));
		busTestService.getBus(1).pickPassengers(stopTestService.getStop(2));
		
		String[] waypoints = stopTestService.buildWaypoints();
		busTestService.setRouteWaypoints(2, "Siltakuja 2, Espoo","Rajamäentie, Espoo", waypoints);
		System.out.println(busTestService.driveCurrentRoute(2));
		
		
		// System.out.println(busTestService.setBusRoute(2, "Siltakuja 2, Espoo",
		// "Kauniainen, Espoo"));
		// System.out.println(busTestService.driveCurrentRoute(2));

		/*
		 * Comment the controller block above and uncomment this to use only the REST
		 * API
		 * 
		 * new BusStopController(new BusStopService()); new BusController(new
		 * BusService());
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