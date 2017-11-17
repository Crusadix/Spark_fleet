package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.BusStop;
import entities.Station;
import interfaces.BusStopInterface;

public class BusStopService {

	private Map<Integer, BusStopInterface> stops = new HashMap<>();

	public List<BusStopInterface> getAllStops() {
		return new ArrayList<>(stops.values());
	}

	public BusStopInterface getStop(int id) {

		return stops.get(id);
	}

	public BusStopInterface createStation(int id, String fuelType, String storageType, String location) {
		BusStopInterface newStop = new Station(id, fuelType, storageType, location);
		stops.put(id, newStop);
		return newStop;
	}

	public BusStopInterface createBusStop(int id, String location) {
		BusStopInterface newStop = new BusStop(id, location);
		stops.put(id, newStop);
		return newStop;
	}

}
