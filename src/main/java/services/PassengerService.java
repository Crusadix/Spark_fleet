package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

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

	// make a list of buses' passengers' destinations and nearby passengers'
	// coordinates and return it
	public String[] buildPassengerWaypoints(VehicleInterface vehicle, String locationCoords, String origin,
			String destination) throws ApiException, InterruptedException, IOException {
		String[] waypointCoords;
		List<PassengerInterface> orderedWaitingPassengersByDistance = new ArrayList<>();
		List<PassengerInterface> passengersOnBoard = vehicle.getPassengersOnBoard();
		for (PassengerInterface passenger : passengers) {
			if (passenger.getStatus().equals("waiting")) {
				orderedWaitingPassengersByDistance.add(passenger);
			}
		}
		orderedWaitingPassengersByDistance = orderByDistance(orderedWaitingPassengersByDistance, origin, destination);
		if ((orderedWaitingPassengersByDistance.size() + passengersOnBoard.size()) >= 8) {
			waypointCoords = new String[8];
		} else {
			waypointCoords = new String[(orderedWaitingPassengersByDistance.size() + passengersOnBoard.size())];
		}
		int onBoardPointer = 0;
		int waitingPassengersPointer = 0;
		for (int y = 0; y < waypointCoords.length; y++) {
			if (onBoardPointer < passengersOnBoard.size() && vehicle.getFreeSeats() > 0) {
				waypointCoords[y] = passengersOnBoard.get(onBoardPointer).getDestinationCoords();
				onBoardPointer++;
			} else if (waitingPassengersPointer < orderedWaitingPassengersByDistance.size()
					&& vehicle.getFreeSeats() > 0) {
				vehicle.addToReservedSeats(orderedWaitingPassengersByDistance.get(waitingPassengersPointer));
				waypointCoords[y] = orderedWaitingPassengersByDistance.get(waitingPassengersPointer).getCurrentCoords();
				orderedWaitingPassengersByDistance.get(waitingPassengersPointer).setStatus("bus on route");
				waitingPassengersPointer++;
			}
		}
		return waypointCoords;
	}

	private List<PassengerInterface> orderByDistance(List<PassengerInterface> waitingPassengers, String origin,
			String destination) throws ApiException, InterruptedException, IOException {
		FleetManager fleetManagement = FleetManager.getInstance();
		BusService busService = fleetManagement.getBusServices().get("Espoo");
		DirectionsRoute routeWithoutPassengers = busService.getRouteString(origin, destination);
		HashMap<Integer, Integer> passengersByDistance = new HashMap<>();
		for (DirectionsStep step : routeWithoutPassengers.legs[0].steps) {
			for (int x = 0; x < waitingPassengers.size(); x++) {
				PassengerInterface tempPassenger = waitingPassengers.get(x);
				double distanceDouble = distanceUtils.getDistanceMeters(step.startLocation.toString(),
						tempPassenger.getCurrentCoords());
				Long L = Math.round(distanceDouble);
				int i = Integer.valueOf(L.intValue());
				if (!passengersByDistance.containsKey(tempPassenger.getId())) {
					passengersByDistance.put(waitingPassengers.get(x).getId(), i);
				} else if (i < passengersByDistance.get(tempPassenger.getId())) {
					passengersByDistance.put(waitingPassengers.get(x).getId(), i);
				}
			}
		}
		List<PassengerInterface> orderedWaitingPassengersByDistance = new ArrayList<>();
		Entry<Integer, Integer> min;
		for (int x = 0; x < waitingPassengers.size(); x++) {
			min = Collections.min(passengersByDistance.entrySet(), Comparator.comparingDouble(Entry::getValue));
			if (min.getValue() < 1500) {
				//System.out.println(min.getKey() + " " + min.getValue());
				orderedWaitingPassengersByDistance.add(getPassenger(min.getKey()));
			}
			passengersByDistance.remove(min.getKey());
		}
		return orderedWaitingPassengersByDistance;
	}
}
