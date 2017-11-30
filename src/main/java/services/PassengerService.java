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
}
