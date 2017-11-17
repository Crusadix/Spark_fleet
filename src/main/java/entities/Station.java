package entities;

import java.util.List;

import interfaces.BusStopInterface;
import interfaces.PassengerInterface;
import interfaces.VehicleInterface;

public class Station implements BusStopInterface {

	private String name = "Station";
	private int id;
	private String fuelingUpType;
	private String storageType;
	private String location;

	public Station(int id, String fuelingUpType, String storageType, String location) {
		this.setId(id);
		this.fuelingUpType = fuelingUpType;
		this.storageType = storageType;
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void addPassenger(PassengerInterface passenger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pickUpPassengers(VehicleInterface vehicle) {
		
		// TODO Auto-generated method stub
		
	}

}
