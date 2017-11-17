package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.RegularPassenger;
import interfaces.PassengerInterface;

public class PassengerService {

	private Map<Integer, PassengerInterface> passengers = new HashMap<>();

	public List<PassengerInterface> getAllPassengers() {
		return new ArrayList<>(passengers.values());
	}

	public PassengerInterface getPassengers(int id) {
		return passengers.get(id);
	}

	public PassengerInterface createPassenger(int id, String origin, String destination) {
		PassengerInterface newPassenger = new RegularPassenger(id, origin, destination);
		passengers.put(id, newPassenger);
		return newPassenger;
	}
	
	
}
