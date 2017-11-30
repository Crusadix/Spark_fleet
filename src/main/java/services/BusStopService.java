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
		return null; //bad practise
	}

	/*
	 * Get coordinates for ALL the currently existing stops!
	 */
	public String[] buildWaypoints() {
		String[] coords = new String[stops.size()];
		for (int x = 0; x < (stops.size()); x++) {
			coords[x] = (stops.get(x).getLocationCoords());
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
		System.out.println("Arrived at " + vehicle.getLocationCoords() + ". Waiting 5 seconds for passengers to get off.");
		Thread.sleep(1);
		for (int x = 0; x < (stops.size()); x++) {
			if (distanceUtils.getDistanceMeters(stops.get(x).getLocationCoords(), vehicle.getLocationCoords()) < 50) {
				List<PassengerInterface> droppingPassangers = new ArrayList<>();
				for (PassengerInterface tempPassenger : vehicle.getPassengersOnBoard()) {
					if (distanceUtils.getDistanceMeters(tempPassenger.getDestinationCoords(), vehicle.getLocationCoords()) < 50) {
						droppingPassangers.add(tempPassenger);
					}
				}
				for (PassengerInterface tempPassenger : droppingPassangers) {
					vehicle.dropPassenger(tempPassenger);
					tempPassenger.setStatus("Delivered");
					System.out.println("Dropped passanger id: " + tempPassenger.getId());
				}
			}
		}
	}

	public void pickUpPassengers(VehicleInterface vehicle) throws InterruptedException {
		System.out.println("Waiting 5 seconds for passengers to get on.");
		Thread.sleep(1);
		for (BusStopInterface busStop : stops) {
			if (distanceUtils.getDistanceMeters(busStop.getLocationCoords(), vehicle.getLocationCoords()) < 50) {
				for (int y = 0; y < busStop.getPassengersWaiting().size();y++) {
					vehicle.pickPassenger(busStop.getPassengersWaiting().get(y));
					busStop.removePassenger(busStop.getPassengersWaiting().get(y));
				}
			}
		}
	}
}
