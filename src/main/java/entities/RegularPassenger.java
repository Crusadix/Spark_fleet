package entities;

import interfaces.PassengerInterface;

public class RegularPassenger implements PassengerInterface {
	
	private int id;
	private String origin;
	private String destination;

	public RegularPassenger(int id, String origin, String destination) {
		this.id = id;
		this.origin = origin;
		this.destination = destination;
	}
}
