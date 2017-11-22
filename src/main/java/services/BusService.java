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
import interfaces.BusStopInterface;
import interfaces.VehicleInterface;
import utilities.FleetManager;
import utilities.MapsSingletonUtils;

public class BusService {

	MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
	FleetManager fleetManagement = FleetManager.getInstance();
	private List<VehicleInterface> buses = new ArrayList<>();
	GeoApiContext context = mapsUtils.getGeoApiContext();
	Gson gson = mapsUtils.getGsonBuilder();
	
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
		System.out.println("DID NOT FIND BUS - BusService getBus");
		return null;
	}

	public VehicleInterface createBus() {
		VehicleInterface newBus = new Ez10(genId());
		buses.add(newBus);
		return newBus;
	}

	public String driveCurrentRoute(int id) throws ApiException, InterruptedException, IOException {
		return getBus(id).driveRoute();
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
		directionsRequest.origin(origin);
		directionsRequest.destination(destination);
		directionsRequest.waypoints(fleetManagement.getBusStopServices().get("Espoo").buildWaypoints());  //build waypoints from ALL currently added bus-stops!
		directionsRequest.optimizeWaypoints(true);
		DirectionsResult result = directionsRequest.await();
		getBus(id).setRoute(result.routes[0]);
		return gson.toJson(result);
	}

}
