package factories;

import java.io.IOException;
import com.google.maps.errors.ApiException;
import entities.RegularPassenger;
import interfaces.PassengerInterface;
import utilities.MapsSingletonUtils;

public class PassengerFactory {

	MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
	private static int passengerId = 0;

	public int genId() {
		PassengerFactory.passengerId++;
		return passengerId;
	}
	
	public PassengerInterface getPassenger(String origin, String destination) throws ApiException, InterruptedException, IOException {
		return new RegularPassenger(genId(), origin, destination, mapsUtils.getGeocode(origin),mapsUtils.getGeocode(destination));
	}
	
}
