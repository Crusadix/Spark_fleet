package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import entities.Ez10;
import interfaces.VehicleInterface;
import utilities.FleetManager;
import utilities.MapsSingletonUtils;

public class BusService {

	FleetManager fleetManagement = FleetManager.getInstance();
	private Map<Integer, VehicleInterface> buses = new HashMap<>();
	MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
	GeoApiContext context = mapsUtils.getGeoApiContext();
	Gson gson = mapsUtils.getGsonBuilder();

	public List<VehicleInterface> getAllBuses() {
		return new ArrayList<>(buses.values());
	}

	public ArrayList<DirectionsStep> getRoute(int id) {
		return buses.get(id).getRoute();
	}

	public VehicleInterface getBus(int id) {
		return buses.get(id);
	}

	public VehicleInterface createBus(int id) {
		VehicleInterface newBus = new Ez10(id);
		buses.put(id, newBus);
		return newBus;
	}

	public String driveCurrentRoute(int id) throws ApiException, InterruptedException, IOException {
		return buses.get(id).moveTo();
	}

	public String setRoute(int id, String origin, String destination)
			throws ApiException, InterruptedException, IOException {
		DirectionsResult result = DirectionsApi.getDirections(context, origin, destination).await();
		buses.get(id).setRoute(result.routes[0]);
		return gson.toJson(result);
	}

	public String setRouteWaypoints(int id, String origin, String destination, String zone)
			throws ApiException, InterruptedException, IOException {
		DirectionsApiRequest directionsRequest = DirectionsApi.newRequest(context);
		directionsRequest.origin(origin);
		directionsRequest.destination(destination);
		directionsRequest.waypoints(fleetManagement.getBusStopServices().get(zone).buildWaypoints());
		directionsRequest.optimizeWaypoints(true);
		DirectionsResult result = directionsRequest.await();
		buses.get(id).setRoute(result.routes[0]);
		return gson.toJson(result);
	}

}
