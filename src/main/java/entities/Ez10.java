package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;

import interfaces.PassengerInterface;
import interfaces.VehicleInterface;

public class Ez10 implements VehicleInterface {

	private String name = "EasyMile EZ10";
	private int id;
	private long batteryLevel;
	private long range;
	private double maxSpeed; // KM/h -- functions as the "Average Speed" of the bus
	private double maxSpeedMeters; // M/S
	private long turnAngle;
	private int width; // CM
	private int length; // CM
	private int maxPassengers = 12;
	private String operatingFuel = "Electric";
	private String operatingType;
	private Map<Integer, DirectionsStep> currentRoute = new HashMap<>();
	private List<PassengerInterface> passengersOnBoard = new ArrayList<>();
	private LatLng location;

	public Ez10(int ezId) {
		this.id = ezId;
		this.batteryLevel = 100;
		this.range = 0;
		this.maxSpeed = 120;
		this.maxSpeedMeters = this.maxSpeed / 3.6;
		this.turnAngle = 35;
		this.width = 200; // CM
		this.length = 250; // CM
		this.maxPassengers = 12;
		this.operatingFuel = "Electric";
		this.operatingType = "onDemand";
	}

	public int getBusId() {
		return id;
	}



	public long getBatteryLevel() {
		return batteryLevel;
	}

	public String setBatteryLevel(String batteryLevel) {
		this.batteryLevel = Integer.valueOf(batteryLevel);
		return batteryLevel;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
		this.maxSpeedMeters = maxSpeed / 3.6;
	}


	public int getMaxPassengers() {
		return maxPassengers;
	}

	public void setMaxPassengers(int maxPassengers) {
		this.maxPassengers = maxPassengers;
	}

	public List<PassengerInterface> getPassengersOnBoard() {
		return passengersOnBoard;
	}


	@Override
	public String moveTo() throws InterruptedException {

		for (int i = 0; i < currentRoute.size(); i++) {
			DirectionsStep nextStep = currentRoute.get(i);
			double timeToMove = nextStep.distance.inMeters / maxSpeedMeters;  //seconds required for moving to next step
			System.out.println("Currently at: " + nextStep.startLocation.toString() + ", moving to "
					+ nextStep.endLocation.toString() + ", moving will take " + timeToMove + "seconds. Current time: "
					+ String.valueOf(System.currentTimeMillis()));
			Thread.sleep((long) timeToMove*1000);
			this.location = nextStep.endLocation;
			System.out.println("Arrived at " + this.location.toString() + ".");
		}
		return "Route completed";
	}

	@Override
	public void fuelUp() {
		// TODO Auto-generated method stub
	}


	@Override
	public void pickPassenger(PassengerInterface passengerInterface) {
		
		
		
		passengersOnBoard.add(passengerInterface);
	}

	@Override
	public void setRoute(DirectionsRoute route) {
		for (int i = 0; i < route.legs[0].steps.length; i++) {
			DirectionsStep step = route.legs[0].steps[i];
			currentRoute.put(i, step);
		}
	}

	public ArrayList<DirectionsStep> getRoute() {
		return new ArrayList<>(currentRoute.values());
	}
}
