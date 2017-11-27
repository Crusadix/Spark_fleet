package entities;

import java.util.ArrayList;
import java.util.List;
import interfaces.*;

public class Station implements BusStopInterface {

	private String name = "Station";
	private int id;
	private String fuelingUpType;
	private String storageType;
	private String location;
	private String locationCoords;
	private List<PassengerInterface> currentPassengers = new ArrayList<>();

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
	
	public void removePassenger(PassengerInterface passenger) {
		currentPassengers.remove(currentPassengers.indexOf(passenger));
	}

	public int getId() {
		return id;
	}

	@Override
	public List<PassengerInterface> getPassengersWaiting() {
		return currentPassengers;
	}


	@Override
	public String getLocationCoords() {
		return locationCoords;
	}
}
