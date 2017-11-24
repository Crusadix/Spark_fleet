package entities;

import java.util.List;
import java.util.Stack;
import interfaces.BusStopInterface;
import interfaces.PassengerInterface;

public class Station implements BusStopInterface {

	private String name = "Station";
	private int id;
	private String fuelingUpType;
	private String storageType;
	private String location;
	private String locationCoords;
	private Stack<PassengerInterface> currentPassengers = new Stack<>();

	public Station(int id, String fuelingUpType, String storageType, String location, String locationCoords) {
		this.id = id;
		this.fuelingUpType = fuelingUpType;
		this.storageType = storageType;
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
	public Stack<PassengerInterface> getPassengersWaiting() {
		return currentPassengers;
	}

	@Override
	public PassengerInterface pickUpPassenger(PassengerInterface passenger) {
		//TODO
		return null;
	}

	@Override
	public String getLocationCoords() {
		return locationCoords;
	}
}
