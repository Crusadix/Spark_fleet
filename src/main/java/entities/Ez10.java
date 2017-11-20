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

import interfaces.BusStopInterface;
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
		this.maxSpeed = 1200;
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
		
		ArrayList<String> derp = new ArrayList<String>(); //testing purposes

		for (int i = 0; i < currentRoute.size(); i++) {
			DirectionsStep nextStep = currentRoute.get(i);
			double timeToMove = nextStep.distance.inMeters / maxSpeedMeters; // seconds required for moving to next step
			//System.out.println(nextStep.startLocation.toString());
			derp.add(nextStep.startLocation.toString());	//testing purposes
			System.out.println("Currently at: " + nextStep.startLocation.toString() + ", moving to "
					+ nextStep.endLocation.toString() + ", moving will take " + timeToMove + "seconds. Current time: "
					+ String.valueOf(System.currentTimeMillis())); 
			
			Thread.sleep((long) timeToMove * 1000);
			this.location = nextStep.endLocation;
			//System.out.println("Arrived at " + this.location.toString() + ". Waiting 10 seconds for passengers.");
			//Thread.sleep(10000);  
		}
		
		for (String y : derp) {
			System.out.println(y); 				//testing purposes
		}
		return "Route completed - All points visited above";

	}

	@Override
	public void fuelUp() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pickPassengers(BusStopInterface busStopInterface) {
		for (int i = 0; i < busStopInterface.getPassengersWaiting().size(); i++) {
			if (passengersOnBoard.size() < maxPassengers) {
				passengersOnBoard.add(busStopInterface.pickUpPassenger());
			}
		}
	}

	@Override
	public void setRoute(DirectionsRoute route) {
		currentRoute = new HashMap<>();
		int y = 0;
		for (int i = 0; i < route.legs.length; i++) {
			for (int x = 0;x < route.legs[i].steps.length; x++) {
				DirectionsStep step = route.legs[i].steps[x];
				currentRoute.put(y, step);
				y++;
			}
		}
	}

	public ArrayList<DirectionsStep> getRoute() {
		return new ArrayList<>(currentRoute.values());
	}
}
