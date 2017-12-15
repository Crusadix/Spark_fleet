package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.maps.errors.ApiException;
import factories.PassengerFactory;
import interfaces.*;
import utilities.*;

public class PassengerService {

	private PassengerFactory passengerFactory = new PassengerFactory();
	private List<PassengerInterface> passengers = new ArrayList<>();
	DistanceUtils distanceUtils = DistanceUtils.getInstance();

	public List<PassengerInterface> getAllPassengers() {
		return passengers;
	}

	public PassengerInterface getPassenger(int id) {
		for (PassengerInterface temp : passengers) {
			if (temp.getId() == id) {
				return temp;
			}
		}
		System.out.println("Passenger not found - PassengerService");
		return null;
	}

	public String createPassenger(String origin, String destination)
			throws ApiException, InterruptedException, IOException {
		passengers.add(passengerFactory.getPassenger(origin, destination));
		return "Passenger created";
	}

	public String moveToBusStop(int passengerId, int stopId) {
		FleetManager fleetManagement = FleetManager.getInstance();
		fleetManagement.getBusStopServices().get("Espoo").addPassenger(stopId, getPassenger(passengerId));
		return "Success";
	}

	// make a list of the nearby passengers' coords and return it
	public String[] buildPassengerWaypoints(String locationCoords) {
		List<PassengerInterface> orderedPassengersByDistance = passengers;
		for (int x = 0; x < orderedPassengersByDistance.size() -1; x++) {
			PassengerInterface tempXPassenger = passengers.get(x);
			PassengerInterface shorterDistanceToXPassenger = passengers.get(x);
			for (int y = x+1; y < orderedPassengersByDistance.size(); y++){
				PassengerInterface tempYPassenger = passengers.get(y);
				if (distanceUtils.getDistanceMeters(locationCoords,
						tempXPassenger.getCurrentCoords()) > distanceUtils.getDistanceMeters(locationCoords,
								tempYPassenger.getCurrentCoords())) {
					shorterDistanceToXPassenger = tempYPassenger;
				}
			}
			orderedPassengersByDistance.set(x, shorterDistanceToXPassenger);
			orderedPassengersByDistance.set(orderedPassengersByDistance.indexOf(shorterDistanceToXPassenger), tempXPassenger);
		}
		String[] closestPassengerCoords;
		if (passengers.size() >= 8) {
			closestPassengerCoords = new String[8];
		}
		else {
			closestPassengerCoords = new String[passengers.size()];
		}
		for (int x = 0; x < closestPassengerCoords.length; x++) {
			closestPassengerCoords[x] = orderedPassengersByDistance.get(x).getCurrentCoords();
		}
		return closestPassengerCoords;
	}
}
