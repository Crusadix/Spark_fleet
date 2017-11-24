package entities;

import java.util.ArrayList;
import java.util.List;
import interfaces.BusStopInterface;
import interfaces.PassengerInterface;

public class BusStop implements BusStopInterface {

	private String name = "Bus stop";
	private int id;
	private String location;
	private String locationCoords;
	private List<PassengerInterface> currentPassengers = new ArrayList<>();

	public BusStop(int id, String location, String locationCoords) {
		this.id = id;
		this.location = location;
		this.locationCoords = locationCoords;
	}

	public void addPassenger(PassengerInterface passenger) {
		currentPassengers.add(passenger);
	}

	public List<PassengerInterface> getPassengers() {
		return currentPassengers;
	}

	public int getId() {
		return id;
	}

	@Override
	public List<PassengerInterface> getPassengersWaiting() {
		return currentPassengers;
	}

	@Override
	public PassengerInterface pickUpPassenger(PassengerInterface passenger) {
		PassengerInterface tempPassenger = currentPassengers.get(currentPassengers.indexOf(passenger));
		currentPassengers.remove(currentPassengers.indexOf(passenger));
		System.out.println("Passenger picked up at: " + location);
		return tempPassenger;
	}

	@Override
	public String getLocationCoords() {
		return locationCoords;
	}
}
