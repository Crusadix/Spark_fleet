package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.maps.errors.ApiException;
import utilities.MapsSingletonUtils;
import entities.BusStop;
import entities.Station;
import interfaces.BusStopInterface;

public class BusStopService {

	private Map<Integer, BusStopInterface> stops = new HashMap<>();
	MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();

	public List<BusStopInterface> getAllStops() {
		return new ArrayList<>(stops.values());
	}

	public BusStopInterface getStop(int id) {
		return stops.get(id);
	}

	public String[] buildWaypoints() {
		String[] coords = new String[stops.size()];
		for (int x = 0; x < (stops.size()); x++) {
			coords[x] = (getStop(x + 1).getLocationCoords());
		}
		return coords;
	}

	public BusStopInterface createStation(int id, String fuelType, String storageType, String location) {
		BusStopInterface newStop = new Station(id, fuelType, storageType, location);
		stops.put(id, newStop);
		return newStop;
	}

	public BusStopInterface createBusStop(int id, String location)
			throws ApiException, InterruptedException, IOException {
		BusStopInterface newStop = new BusStop(id, location, mapsUtils.getGeocode(location));
		stops.put(id, newStop);
		return newStop;
	}

}
