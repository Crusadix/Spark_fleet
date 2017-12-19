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

	// make a list of buses' passengers destinations and nearby passengers' coords and return it
	public String[] buildPassengerWaypoints(VehicleInterface vehicle, String locationCoords) {
		String[] closestPassengerCoords;
		List<PassengerInterface> orderedPassengersByDistance = new ArrayList<>();
		List<PassengerInterface> passengersOnBoard = new ArrayList<>();
		int pointer = 0;
		for (PassengerInterface passenger : vehicle.getPassengersOnBoard()) {
			boolean matchFound = false;
			for (PassengerInterface passengerCheckDestinationDuplicate : passengersOnBoard) {
				if (passenger.getDestinationCoords().equals(passengerCheckDestinationDuplicate.getDestinationCoords())) {
					matchFound = true; 
				}
			}
			if (!matchFound) {
				passengersOnBoard.add(passenger);
			}
		}
		
		for (PassengerInterface passenger : passengers) {
			if (passenger.getStatus().equals("waiting")) {
				boolean matchFound = false;
				for (PassengerInterface passengerCheckDestinationDuplicate : orderedPassengersByDistance) {
					if (passenger.getDestinationCoords().equals(passengerCheckDestinationDuplicate.getDestinationCoords())) {
						matchFound = true; 
					}
				}
				if (!matchFound) {
					orderedPassengersByDistance.add(passenger);
				}
			}
		}
		
		if ((orderedPassengersByDistance.size() + passengersOnBoard.size()) >= 8) {
			closestPassengerCoords = new String[8];
		}
		else {
			closestPassengerCoords = new String[(orderedPassengersByDistance.size() + passengersOnBoard.size())];
		}
		
		for (; pointer < passengersOnBoard.size(); pointer++) {
			closestPassengerCoords[pointer] = passengersOnBoard.get(pointer).getDestinationCoords();
			if (pointer == 7) {
				break;
			}
			
		}
		for (int x = 0; x < orderedPassengersByDistance.size() -1; x++) {
			PassengerInterface tempXPassenger = orderedPassengersByDistance.get(x);
			PassengerInterface shorterDistanceToXPassenger = orderedPassengersByDistance.get(x);
			for (int y = x+1; y < orderedPassengersByDistance.size(); y++){
				PassengerInterface tempYPassenger = orderedPassengersByDistance.get(y);
				if (distanceUtils.getDistanceMeters(locationCoords,
						tempXPassenger.getCurrentCoords()) > distanceUtils.getDistanceMeters(locationCoords,
								tempYPassenger.getCurrentCoords())) {
					shorterDistanceToXPassenger = tempYPassenger;
				}
			}
			orderedPassengersByDistance.set(x, shorterDistanceToXPassenger);
			orderedPassengersByDistance.set(orderedPassengersByDistance.indexOf(shorterDistanceToXPassenger), tempXPassenger);
		}
		for (int x = 0; x < closestPassengerCoords.length -1; x++) {
			closestPassengerCoords[pointer] = orderedPassengersByDistance.get(x).getCurrentCoords();
			if (pointer == 7) {
				break;
			}
			pointer ++;
		}

		return closestPassengerCoords;
	}
}
