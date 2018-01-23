package entities;

import interfaces.*;
import services.*;
import utilities.*;

public class RegularPassenger implements PassengerInterface {

	private int id;
	private String origin;
	private String destination;
	private String originCoords;
	private String destinationCoords;
	private String status;
	private String currentCoords;

	public RegularPassenger(int id, String origin, String destination, String originCoords, String destinationCoords) {
		this.id = id;
		this.origin = origin;
		this.destination = destination;
		this.status = "waiting";
		this.originCoords = originCoords; 
		this.currentCoords = originCoords; 
		this.destinationCoords = destinationCoords;
		addPassengerToNearbyStop();
	}
	
	private void addPassengerToNearbyStop() {
		FleetManager fleetManagement = FleetManager.getInstance();
		DistanceUtils distanceUtils = DistanceUtils.getInstance();
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		for (int x = 0; x < (busStopService.getAllStops().size()); x++) {
			if (distanceUtils.getDistanceMeters(busStopService.getAllStops().get(x).getLocationCoords(),
					originCoords) < 50) {
				busStopService.addPassenger(busStopService.getAllStops().get(x).getId(), this);
			}
		}
	}
	
	@Override
	public void setCurrentCoords(String currentCoords) {
		this.currentCoords = currentCoords;
	}
	
	@Override
	public String getCurrentCoords() {
		return currentCoords;
	}
	
	@Override
	public String getOriginCoords() {
		return originCoords;
	}
	
	@Override
	public String getDestinationCoords() {
		return destinationCoords;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setStatus(String newStatus) {
		this.status = newStatus;
	}
	
	@Override
	public String getStatus() {
		return status;
	}
}
