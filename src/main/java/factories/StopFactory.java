package factories;

import java.io.IOException;
import com.google.maps.errors.ApiException;
import entities.BusStop;
import entities.Station;
import interfaces.BusStopInterface;
import utilities.MapsSingletonUtils;

public class StopFactory {
	
	MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
	private static int stopId = 0;

	public int genId() {
		StopFactory.stopId++;
		return stopId;
	}
	
	public BusStopInterface getStop(String fuelType, String storageType, String location) throws ApiException, InterruptedException, IOException {
		return new Station(genId(), fuelType, storageType, location, mapsUtils.getGeocode(location));
	}
	
	public BusStopInterface getStop(String location) throws ApiException, InterruptedException, IOException {
		return new BusStop(genId(), location, mapsUtils.getGeocode(location));
	}
	
}
