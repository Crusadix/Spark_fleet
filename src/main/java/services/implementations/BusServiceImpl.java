package services.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import factories.*;
import interfaces.*;
import services.*;
import utilities.*;

public class BusServiceImpl implements BusService{

		FleetManager fleetManagement = FleetManager.getInstance();
		GeoApiContext context = MapsSingletonUtils.getGeoApiContext();
		Gson gson = MapsSingletonUtils.getGsonBuilder();
		private List<VehicleInterface> buses = new ArrayList<>();
		BusFactory busFactory = new BusFactory();
		String region;
	

		@Override
		public List<VehicleInterface> getAllBuses() {
			return buses;
		}

		@Override
		public VehicleInterface getBus(int busId) {
			for (VehicleInterface tempBus : buses) {
				if (tempBus.getId() == busId) {
					return tempBus;
				}
			}
			throw new IllegalArgumentException("Error has occurred in BusService - bus not found");
		}

		@Override
		public VehicleInterface createBus(String location) throws ApiException, InterruptedException, IOException {
			VehicleInterface newVehicle = busFactory.getVehicle(location);
			buses.add(newVehicle);
			return newVehicle;
		}

		@Override
		public boolean driveCurrentRoute(int id, String operationType) throws ApiException, InterruptedException, IOException {
			getBus(id).setOperatingType(operationType);
			getBus(id).driveRoute(); 
			return true;
		}

		@Override
		public DirectionsRoute getRouteSimple(LatLng originLatLon, LatLng destinationLatLon)
				throws ApiException, InterruptedException, IOException {
			DirectionsResult result = DirectionsApi.getDirections(context, originLatLon.toString(), destinationLatLon.toString()).await();
			return result.routes[0];  // the first route of the list is the "shortest" current one from the google responses
		}

		@Override
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
		
		@Override
		public DirectionsResult setRouteWaypointsOnDemand(int busId, String origin, String destination, String zone)
				throws ApiException, InterruptedException, IOException {
			DirectionsApiRequest directionsRequest = DirectionsApi.newRequest(context);
			PassengerService passengerService = fleetManagement.getPassengerServices().get("Espoo");
			directionsRequest.origin(origin);
			directionsRequest.destination(destination);
			directionsRequest.waypoints(passengerService.buildPassengerWaypoints(getBus(busId), origin, destination));
			directionsRequest.optimizeWaypoints(true);
			DirectionsResult result = directionsRequest.await();
			getBus(busId).setIntendedRoute(result.routes[0]);
			getBus(busId).setRouteResults(result);
			return result;
		}
	}
