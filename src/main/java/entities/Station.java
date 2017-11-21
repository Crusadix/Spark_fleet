package entities;

import java.util.Stack;
import interfaces.BusStopInterface;
import interfaces.PassengerInterface;

public class Station implements BusStopInterface {

	private String name = "Station";
	private int id;
	private String fuelingUpType;
	private String storageType;
	private String location;

	public Station(int id, String fuelingUpType, String storageType, String location) {
		this.id= id;
		this.fuelingUpType = fuelingUpType;
		this.storageType = storageType;
		this.location = location;
	}

	@Override
	public void addPassenger(PassengerInterface passenger) {
		// TODO Auto-generated method stub
	}

	@Override
	public Stack<PassengerInterface> getPassengersWaiting() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PassengerInterface pickUpPassenger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocationCoords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
