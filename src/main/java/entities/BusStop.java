package entities;

import java.util.List;
import java.util.Stack;
import interfaces.BusStopInterface;
import interfaces.PassengerInterface;

public class BusStop implements BusStopInterface {

	private String name = "Bus stop";
	private int id;
	private String location;
	private String locationCoords; 
	private Stack<PassengerInterface> currentPassengers = new Stack<>();

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
	public Stack<PassengerInterface> getPassengersWaiting() {
		return currentPassengers;
	}

	@Override
	public PassengerInterface pickUpPassenger() {
		System.out.println("Passenger picked up at: " + location);
		return currentPassengers.pop();
	}

	@Override
	public String getLocationCoords() {
		return locationCoords;
	}
}
