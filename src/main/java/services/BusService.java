package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import factories.BusFactory;
import interfaces.*;
import utilities.*;

public class BusService {

	FleetManager fleetManagement = FleetManager.getInstance();
	GeoApiContext context = MapsSingletonUtils.getGeoApiContext();
	Gson gson = MapsSingletonUtils.getGsonBuilder();
	private List<VehicleInterface> buses = new ArrayList<>();
	BusFactory busFactory = new BusFactory();

	public List<VehicleInterface> getAllBuses() {
		return buses;
	}

	public List<DirectionsStep> getRoute(int busId) {
		return getBus(busId).getRoute();
	}

	public VehicleInterface getBus(int busId) {
		for (VehicleInterface tempBus : buses) {
			if (tempBus.getId() == busId) {
				return tempBus;
			}
		}
		Log.error("Error has occurred in BusService - bus not found"); // Needs implementation of fault tolerance - returning null is bad practice
		return null;
	}

	public VehicleInterface createBus(String location) throws ApiException, InterruptedException, IOException {
		VehicleInterface newVehicle = busFactory.getVehicle(location);
		buses.add(newVehicle);
		return newVehicle;
	}

	public boolean driveCurrentRoute(int id, String operationType) throws ApiException, InterruptedException, IOException {
		getBus(id).setOperatingType(operationType);
		getBus(id).driveRoute(); 
		return true;
	}

	public boolean setIntendedRoute(int id, String origin, String destination)
			throws ApiException, InterruptedException, IOException {
		DirectionsResult result = DirectionsApi.getDirections(context, origin, destination).await();
		getBus(id).setIntendedRoute(result.routes[0]);
		getBus(id).setRouteResults(result);
		return true;
	}

	public DirectionsRoute getRouteLatLon(String originLatLon, String destinationLatLon)
			throws ApiException, InterruptedException, IOException {
		DirectionsResult result = DirectionsApi.getDirections(context, originLatLon, destinationLatLon).await();
		return result.routes[0];  // the first route of the list is the "shortest" current one from the google responses
	}
	
	public DirectionsRoute getRouteString(String origin, String destination)
			throws ApiException, InterruptedException, IOException {
		DirectionsResult result = DirectionsApi.getDirections(context, origin, destination).await();
		return result.routes[0]; // the first route of the list is the "shortest" current one from the google responses
	}

	public DirectionsResult setRouteWaypoints(int busId, String origin, String destination, String waypoints, String zone)
			throws ApiException, InterruptedException, IOException {
		DirectionsApiRequest directionsRequest = DirectionsApi.newRequest(context);
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		directionsRequest.origin(origin);
		directionsRequest.destination(destination);
		if (!waypoints.equals("0")) {
			directionsRequest.waypoints(busStopService.buildWaypoints(waypoints)); 	
			directionsRequest.optimizeWaypoints(true);
		}
		DirectionsResult result = directionsRequest.await();
		getBus(busId).setIntendedRoute(result.routes[0]);
		getBus(busId).setRouteResults(result);
		return result;
	}
	
	public DirectionsResult setRouteWaypointsOnDemand(int busId, String origin, String destination, String zone)
			throws ApiException, InterruptedException, IOException {
		MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
		DirectionsApiRequest directionsRequest = DirectionsApi.newRequest(context);
		PassengerService passengerService = fleetManagement.getPassengerServices().get("Espoo");
		BusService busService = fleetManagement.getBusServices().get("Espoo");
		directionsRequest.origin(origin);
		directionsRequest.destination(destination);
		directionsRequest.waypoints(passengerService.buildPassengerWaypoints(busService.getBus(busId), mapsUtils.getGeocode(origin), origin, destination));
		directionsRequest.optimizeWaypoints(true);
		DirectionsResult result = directionsRequest.await();
		getBus(busId).setIntendedRoute(result.routes[0]);
		getBus(busId).setRouteResults(result);
		return result;
	}
}
