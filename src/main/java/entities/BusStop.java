package entities;

import java.util.List;
import java.util.Stack;
import interfaces.BusStopInterface;
import interfaces.PassengerInterface;
import interfaces.VehicleInterface;

public class BusStop implements BusStopInterface {

	private String name = "Bus stop";
	private int id;
	private String location;
	private Stack<PassengerInterface> currentPassengers = new Stack<>();

	public BusStop(int id, String location) {
		this.id = id;
		this.location = location;
	}

	public void addPassenger(PassengerInterface passenger) {
		currentPassengers.add(passenger);
	}

	public List<PassengerInterface> getPassengers() {
		return currentPassengers;
	}

	@Override
	public void pickUpPassengers(VehicleInterface vehicle) {

		for (int i = 0; i < currentPassengers.size(); i++) {
			System.out.println("Stop derp");
			if (vehicle.getPassengersOnBoard().size() < vehicle.getMaxPassengers()) {
				vehicle.pickPassenger(currentPassengers.pop());
			}
		}
	}
}
