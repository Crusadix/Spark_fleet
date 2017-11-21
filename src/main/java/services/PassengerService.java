package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.RegularPassenger;
import interfaces.BusStopInterface;
import interfaces.PassengerInterface;
import utilities.FleetManager;

public class PassengerService {

	private List<PassengerInterface> passengers = new ArrayList<>();
	FleetManager fleetManagement = FleetManager.getInstance();
	

	public List<PassengerInterface> getAllPassengers() {
		return passengers;
	}

	public PassengerInterface getPassengers(int id) {
		for (PassengerInterface temp : passengers) {
			if (temp.getId() == id) {
				return temp;
			}
		}
		return null;
	}

	public PassengerInterface createPassenger(int id, String origin, String destination) {
		PassengerInterface newPassenger = new RegularPassenger(id, origin, destination);
		passengers.add(newPassenger);
		return newPassenger;
	}

	public String moveToBusStop(int passengerId, int stopId) {
		fleetManagement.getBusStopServices().get("Espoo").addPassenger(stopId, getPassengers(passengerId));
		return "Success";
	}

}
