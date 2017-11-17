package entities;

import interfaces.BusStopInterface;

public class Station implements BusStopInterface {

	private String name = "Station";
	private int id;
	private String fuelingUpType;
	private String storageType;
	private int passengers;
	private String location;

	public Station(int id, String fuelingUpType, String storageType, String location) {
		this.setId(id);
		this.fuelingUpType = fuelingUpType;
		this.storageType = storageType;
		this.passengers = 0;
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void pickUpPassengers(int amount) {
		// TODO Auto-generated method stub

	}
}
