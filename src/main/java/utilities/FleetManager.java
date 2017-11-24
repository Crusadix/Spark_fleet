package utilities;

import java.util.ArrayList;
import java.util.HashMap;

import services.BusService;
import services.BusStopService;
import services.PassengerService;

public class FleetManager {

	private static FleetManager fleetManager = new FleetManager();
	
	private HashMap<String, BusService> busServices = new HashMap<String, BusService>();
	private HashMap<String,BusStopService> busStopServices = new HashMap<String,BusStopService>();
	private HashMap<String,PassengerService> passengerServices = new HashMap<String,PassengerService>();
	
	private FleetManager() {
	}
	
	public void addBusService (String zone, BusService busService) {
		busServices.put(zone, busService);
	}
	public void addBusStopService (String zone,BusStopService busStopService) {
		busStopServices.put(zone, busStopService);
	}
	public void addPassengerService (String zone,PassengerService passengerService) {
		passengerServices.put(zone, passengerService);
	}
	
	public HashMap<String, BusService> getBusServices() {
		return busServices;
	}

	public HashMap<String, BusStopService> getBusStopServices() {
		return busStopServices;
	}

	public HashMap<String, PassengerService> getPassengerServices() {
		return passengerServices;
	}

	public static FleetManager getInstance() {
		return fleetManager;
	}
}
