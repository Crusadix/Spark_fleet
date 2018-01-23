package factories;

import java.io.IOException;

import com.google.maps.errors.ApiException;

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

	public VehicleInterface getVehicle(String location) throws ApiException, InterruptedException, IOException {
		return new Ez10(genId(), location);
	}
}
