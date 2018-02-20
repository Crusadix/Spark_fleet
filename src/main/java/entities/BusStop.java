package entities;

import java.util.ArrayList;
import java.util.List;

import com.google.maps.model.LatLng;

import interfaces.*;

public class BusStop implements BusStopInterface {

	private String name = "Bus stop";
	private int id;
	private String location;
	private LatLng locationCoords;
	private List<PassengerInterface> currentPassengers = new ArrayList<>();

	public BusStop(int id, String location, LatLng locationCoords) {
		this.id = id;
		this.location = location;
		this.locationCoords = locationCoords;
	}

	public void addPassenger(PassengerInterface passenger) {
		currentPassengers.add(passenger);
	}
	
	public void removePassenger(PassengerInterface passenger) {
		currentPassengers.remove(currentPassengers.indexOf(passenger));
	}

	public int getId() {
		return id;
	}

	@Override
	public List<PassengerInterface> getPassengersWaiting() {
		return currentPassengers;
	}

	@Override
	public LatLng getLocationCoords() {
		return locationCoords;
	}
	@Override
	public void fuelVehicle (VehicleInterface vehicle) {
		System.out.println("Unsupported for bus-stops");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLocation() {
		return location;
	}
}
