package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.maps.errors.ApiException;
import utilities.*;
import factories.StopFactory;
import interfaces.*;

public class BusStopService {

	DistanceUtils distanceUtils = DistanceUtils.getInstance();
	private List<BusStopInterface> stops = new ArrayList<>();
	StopFactory stopFactory = new StopFactory();

	public List<BusStopInterface> getAllStops() {
		return stops;
	}

	public BusStopInterface getStop(int id) {
		for (BusStopInterface temp : stops) {
			if (temp.getId() == id) {
				return temp;
			}
		}
		System.out.println("Stop not found - BusStopService");
		return null; //bad practice
	}

	/*
	 * Optimistic - if not found, return faulty list
	 */
	public String[] buildWaypoints(String waypoints) {
		if (waypoints.length() < 2) {
			String[] coords = {getStop(Integer.parseInt(waypoints)).getLocationCoords()};
			return coords;
		}
		String[] parts = waypoints.split(",");
		String[] coords = new String[parts.length];
		for (int y = 0; y < parts.length; y++) {
			for (int x = 0; x < stops.size(); x++) {
				if (Integer.parseInt(parts[y]) == stops.get(x).getId()) {
					coords[y] = (stops.get(x).getLocationCoords());
				}
			}
		}
		return coords;
	}

	public String createStation(String fuelType, String storageType, String location) throws ApiException, InterruptedException, IOException {
		stops.add(stopFactory.getStop(fuelType, storageType, location));
		return "Station created";
	}

	public String createBusStop(String location) throws ApiException, InterruptedException, IOException {
		stops.add(stopFactory.getStop(location));
		return "BusStop created";
	}

	public void addPassenger(int stopId, PassengerInterface passenger) {
		getStop(stopId).addPassenger(passenger);
	}

	/*
	 * Checks distance to all stops, then checks whether passenger destination is
	 * close to the stops close-by. 
	 */
	public void dropOffPassengers(VehicleInterface vehicle) throws InterruptedException {
		for (int x = 0; x < (stops.size()); x++) {
			if (distanceUtils.getDistanceMeters(stops.get(x).getLocationCoords(), vehicle.getLocationCoords()) < 50) {
				System.out.println("Arrived at " + vehicle.getLocationCoords() + ". Waiting 5 seconds for passengers to get off.");
				Double timeToWaitAtDestination = 5.00;	//facilitates web-page to wait before asking directions
				vehicle.setTimeToCurrentDestination(timeToWaitAtDestination);
				Thread.sleep(5000);
				List<PassengerInterface> droppingPassangers = new ArrayList<>();
				for (PassengerInterface tempPassenger : vehicle.getPassengersOnBoard()) {
					if (distanceUtils.getDistanceMeters(tempPassenger.getDestinationCoords(), vehicle.getLocationCoords()) < 50) {
						droppingPassangers.add(tempPassenger);
					}
				}
				for (PassengerInterface tempPassenger : droppingPassangers) {
					vehicle.dropPassenger(tempPassenger);
				}
			}
		}
	}

	public void pickUpPassengers(VehicleInterface vehicle) throws InterruptedException {
		for (BusStopInterface busStop : stops) {
			if (distanceUtils.getDistanceMeters(busStop.getLocationCoords(), vehicle.getLocationCoords()) < 50) {
				System.out.println("Waiting 5 seconds for passengers to get on.");
				Double timeToWaitAtDestination = 5.00; //facilitates web-page to wait before asking directions
				vehicle.setTimeToCurrentDestination(timeToWaitAtDestination);
				Thread.sleep(5000);
				List<PassengerInterface> pickingPassangers = new ArrayList<>();
				for (int y = 0; y < busStop.getPassengersWaiting().size();y++) {
					for (BusStopInterface endBusStop : stops) {
						if (distanceUtils.getDistanceMeters(busStop.getPassengersWaiting().get(y).getDestinationCoords(), endBusStop.getLocationCoords()) < 50) {
							pickingPassangers.add(busStop.getPassengersWaiting().get(y));
						}
					}
				}
				for (PassengerInterface passenger : pickingPassangers) {
					vehicle.pickPassenger(passenger);
					busStop.removePassenger(passenger);
				}
			}
		}
	}
}
