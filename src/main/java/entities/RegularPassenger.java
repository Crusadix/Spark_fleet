package entities;

import com.google.maps.model.LatLng;
import enums.PassengerStatus;
import interfaces.*;
import services.*;
import utilities.*;

public class RegularPassenger implements PassengerInterface {

	private int id;
	private String origin;
	private String destination;
	private LatLng originCoords;
	private LatLng destinationCoords;
	private PassengerStatus status;
	private LatLng currentCoords;

	public RegularPassenger(int id, String origin, String destination, LatLng originCoords, LatLng destinationCoords) {
		this.id = id;
		this.origin = origin;
		this.destination = destination;
		this.status = PassengerStatus.waiting;
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
	public void setCurrentCoords(LatLng currentCoords) {
		this.currentCoords = currentCoords;
	}
	
	@Override
	public LatLng getCurrentCoords() {
		return currentCoords;
	}
	
	@Override
	public LatLng getOriginCoords() {
		return originCoords;
	}
	
	@Override
	public LatLng getDestinationCoords() {
		return destinationCoords;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setStatus(PassengerStatus newStatus) {
		this.status = newStatus;
	}
	
	@Override
	public PassengerStatus getStatus() {
		return status;
	}
}
