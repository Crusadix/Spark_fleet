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

	// make a list of buses' passengers' destinations and nearby passengers' coords
	// and return it
	public String[] buildPassengerWaypoints(VehicleInterface vehicle, String locationCoords) {
		String[] waypointCoords;
		List<PassengerInterface> orderedWaitingPassengersByDistance = new ArrayList<>();
		List<PassengerInterface> passengersOnBoard = vehicle.getPassengersOnBoard();
		for (PassengerInterface passenger : passengers) {
			if ((passenger.getStatus().equals("waiting")) && ((passengersOnBoard.size() + orderedWaitingPassengersByDistance.size()) < vehicle.getFreeSeats())) {
				orderedWaitingPassengersByDistance.add(passenger);
			}
		}
		orderedWaitingPassengersByDistance = orderByDistance(orderedWaitingPassengersByDistance, locationCoords);

		if ((orderedWaitingPassengersByDistance.size() + passengersOnBoard.size()) >= 8) {
			waypointCoords = new String[8];
		} else {
			waypointCoords = new String[(orderedWaitingPassengersByDistance.size() + passengersOnBoard.size())];
		}
		int onBoardPointer = 0; 
		int waitingPassengersPointer = 0;
		for (int y = 0; y < waypointCoords.length; y++) {
			if (onBoardPointer < passengersOnBoard.size()) {
				waypointCoords[y] = passengersOnBoard.get(onBoardPointer).getDestinationCoords();
				onBoardPointer++;
			}
			else if(waitingPassengersPointer < orderedWaitingPassengersByDistance.size()) {
				vehicle.addToReservedSeats(orderedWaitingPassengersByDistance.get(waitingPassengersPointer));
				waypointCoords[y] = orderedWaitingPassengersByDistance.get(waitingPassengersPointer).getCurrentCoords();
				orderedWaitingPassengersByDistance.get(waitingPassengersPointer).setStatus("bus on route");
				waitingPassengersPointer++;
			}
		}
		return waypointCoords;
	}

	private List<PassengerInterface> orderByDistance(List<PassengerInterface> orderedWaitingPassengersByDistance,
			String locationCoords) {
		for (int x = 0; x < orderedWaitingPassengersByDistance.size() - 1; x++) {
			PassengerInterface tempXPassenger = orderedWaitingPassengersByDistance.get(x);
			PassengerInterface shorterDistanceToXPassenger = orderedWaitingPassengersByDistance.get(x);
			for (int y = x + 1; y < orderedWaitingPassengersByDistance.size(); y++) {
				PassengerInterface tempYPassenger = orderedWaitingPassengersByDistance.get(y);
				if (distanceUtils.getDistanceMeters(locationCoords, tempXPassenger.getCurrentCoords()) > distanceUtils
						.getDistanceMeters(locationCoords, tempYPassenger.getCurrentCoords())) {
					shorterDistanceToXPassenger = tempYPassenger;
				}
			}
			orderedWaitingPassengersByDistance.set(x, shorterDistanceToXPassenger);
			orderedWaitingPassengersByDistance
					.set(orderedWaitingPassengersByDistance.indexOf(shorterDistanceToXPassenger), tempXPassenger);
		}
		return orderedWaitingPassengersByDistance;
	}
}
