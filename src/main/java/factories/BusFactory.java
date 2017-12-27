package factories;

import entities.Ez10;
import interfaces.VehicleInterface;

public class BusFactory {

	private static int busId = 0;

	public BusFactory() {
	}

	public int genId() {
		BusFactory.busId++;
		return busId;
	}

	public VehicleInterface getVehicle() {
		return new Ez10(genId());
	}
}
