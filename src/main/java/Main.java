import static spark.Spark.*;

import java.io.IOException;

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
import services.BusService;
import services.BusStopService;

public class Main {
	

	public static void main(String[] args) {

	/*	
		GeoApiContext context = new GeoApiContext.Builder()
			    .apiKey("AIzaSyCnKfFi02VENIr0EXMND16fdBYSjxfncHA")
			    .build();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

			//GeocodingResult[] results = GeocodingApi.geocode(context,"Siltakuja 2, espoo").await();
			//System.out.println(gson.toJson(results[0].addressComponents));
	
			DirectionsResult results = null;
			try {
				results = DirectionsApi.getDirections(context, "Siltakuja 2, Espoo", "Sello, Espoo").await();
			} catch (ApiException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
				System.out.println(gson.toJson(results));  */
				
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
		stopTestService.createStation(1, "Electric", "Warehouse", "Sello, Espoo");
		stopTestService.createBusStop(2, "Siltakuja, Espoo");

		// new BusController(new BusService()); //comment the controller block above and
		// uncomment this to use only the REST API depicted in BusService.java
	}

	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
	}
}