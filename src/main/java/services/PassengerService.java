package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.maps.errors.ApiException;
import entities.RegularPassenger;
import interfaces.PassengerInterface;
import utilities.FleetManager;

public class PassengerService {

	private List<PassengerInterface> passengers = new ArrayList<>();
	private static int passengerId = 0;

	public int genId() {
		PassengerService.passengerId++;
		return passengerId;
	}

	public List<PassengerInterface> getAllPassengers() {
		for (PassengerInterface temp : passengers) {
			System.out.println(temp.getId());
		}
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

	public PassengerInterface createPassenger(String origin, String destination)
			throws ApiException, InterruptedException, IOException {
		PassengerInterface newPassenger = new RegularPassenger(genId(), origin, destination);
		passengers.add(newPassenger);
		return newPassenger;
	}

	public String moveToBusStop(int passengerId, int stopId) {
		FleetManager fleetManagement = FleetManager.getInstance();
		fleetManagement.getBusStopServices().get("Espoo").addPassenger(stopId, getPassenger(passengerId));
		return "Success";
	}
}
