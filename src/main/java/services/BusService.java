package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import entities.*;
import interfaces.*;
import utilities.*;

public class BusService {

	FleetManager fleetManagement = FleetManager.getInstance();
	GeoApiContext context = MapsSingletonUtils.getGeoApiContext();
	Gson gson = MapsSingletonUtils.getGsonBuilder();
	private List<VehicleInterface> buses = new ArrayList<>();
	private static int busId = 0;
	
	public int genId() {
		BusService.busId ++;
		return busId;
	}

	public List<VehicleInterface> getAllBuses() {
		return buses;
	}

	public List<DirectionsStep> getRoute(int id) {
		return getBus(id).getRoute();
	}

	public VehicleInterface getBus(int id) {
		for (VehicleInterface temp : buses) {
			if (temp.getId() == id) {
				return temp;
			}
		}
		System.out.println("Error has occurred in BusService - bus not found");  //Needs implementation of fault tolerance - returning null is bad practice
		return null;
	}

	public VehicleInterface createBus() {
		VehicleInterface newBus = new Ez10(genId());
		buses.add(newBus);
		return newBus;
	}

	public String driveCurrentRoute(int id) throws ApiException, InterruptedException, IOException {
		getBus(id).driveRoute();
		return "Driving current route, bus number: " + id;
	}

	public String setRoute(int id, String origin, String destination)
			throws ApiException, InterruptedException, IOException {
		DirectionsResult result = DirectionsApi.getDirections(context, origin, destination).await();
		getBus(id).setRoute(result.routes[0]);
		return gson.toJson(result);
	}

	public String setRouteWaypoints(int id, String origin, String destination, String zone)
			throws ApiException, InterruptedException, IOException {
		DirectionsApiRequest directionsRequest = DirectionsApi.newRequest(context);
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		directionsRequest.origin(origin);
		directionsRequest.destination(destination);
		directionsRequest.waypoints(busStopService.buildWaypoints());  //build waypoints from ALL currently added bus-stops!
		directionsRequest.optimizeWaypoints(true);
		DirectionsResult result = directionsRequest.await();
		getBus(id).setRoute(result.routes[0]);
		return gson.toJson(result);
	}

}
