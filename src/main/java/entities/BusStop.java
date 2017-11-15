package entities;

import interfaces.BusStopInterface;

public class BusStop implements BusStopInterface{

	int id;
	int currentPassengers = 0;
	String location;
	
	
	public BusStop (int id, String location) {
		this.id = id;
		this.location = location;
	}
	
	
	public void setPassengers(int amount){
		
		currentPassengers = currentPassengers + amount;
	}
	
	public int getPassengers() {
		return currentPassengers;
	}

	@Override
	public void pickUpPassengers(int amount) {
		// TODO Auto-generated method stub
		
	}
}

