package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Ez10;
import interfaces.VehicleInterface;

public class BusService {

	private Map<Integer, VehicleInterface> buses = new HashMap<>();

	public List<VehicleInterface> getAllBuses() {
		return new ArrayList<>(buses.values());
	}
	
	public VehicleInterface getBus(int id) {
		
		return buses.get(id);
	}
	
	public VehicleInterface createBus(int id) {
		VehicleInterface newBus = new Ez10(id);
		buses.put(id, newBus);
		return newBus;
	}
	
}
