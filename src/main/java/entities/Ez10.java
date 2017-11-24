package entities;

import java.util.ArrayList;
import java.util.List;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;
import interfaces.BusStopInterface;
import interfaces.PassengerInterface;
import interfaces.VehicleInterface;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import services.BusStopService;
import utilities.FleetManager;

public class Ez10 implements VehicleInterface {

	private String name = "EasyMile EZ10";
	private long batteryLevel;
	private int id;
	private long range;  	 // Amount of seconds still operable
	private double maxSpeed; // KM/h -- functions as the "Average Speed" of the bus
	private double maxSpeedMeters; // M/S
	private long turnAngle;
	private int width; // CM
	private int length; // CM
	private int height; // CM
	private int maxPassengers = 12;
	private String operatingFuel = "Electric";
	private String operatingType;
	private List<DirectionsStep> currentRoute = new ArrayList<>();
	private List<PassengerInterface> passengersOnBoard = new ArrayList<>();
	private LatLng location;

	public Ez10(int id) {
		this.id = id;
		this.batteryLevel = 100;
		this.range = 14 * 60 *60;
		this.maxSpeed = 1200;
		this.maxSpeedMeters = this.maxSpeed / 3.6;
		this.turnAngle = 35;
		this.width = 200; // CM
		this.length = 400; // CM
		this.height = 275; // CM
		this.maxPassengers = 12;
		this.operatingFuel = "Electric";
		this.operatingType = "Metro";  // Stop on every bus-stop
	}

	public int getId() {
		return id;
	}

	public String setBatteryLevel(String batteryLevel) {
		this.batteryLevel = Integer.valueOf(batteryLevel);
		return batteryLevel;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
		this.maxSpeedMeters = maxSpeed / 3.6;
	}

	public List<PassengerInterface> getPassengersOnBoard() {
		return passengersOnBoard;
	}

	/*
	 * Needs implementation of adding a fueling-station to current path, making sure the range is enough to reach it!
	 * VERY MESSY CODE - FIX IT
	 */ 
	@Override
	public void driveRoute() throws InterruptedException {
		FleetManager fleetManagement = FleetManager.getInstance();
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		ArrayList<String> pointsVisited = new ArrayList<String>(); 							// testing purposes
		System.out.println("Current range: " +range);

		Flowable.fromCallable(() -> {
		for (int i = 0; i < currentRoute.size(); i++) {
			DirectionsStep nextStep = currentRoute.get(i);
			double timeToMove = nextStep.distance.inMeters / maxSpeedMeters;		// seconds required for moving to next step
			pointsVisited.add(nextStep.startLocation.toString()); 					// testing purposes - for plotting all the steps on a map
			System.out.println("Currently at: " + nextStep.startLocation.toString() + ", moving to "
					+ nextStep.endLocation.toString() + ", moving will take " + timeToMove + "seconds. Current time: "
					+ String.valueOf(System.currentTimeMillis()));
			simulateDrive(timeToMove);
			this.location = nextStep.endLocation;
			System.out.println("Arrived at " + this.location.toString() + ". Waiting 10 seconds for passengers.");
			busStopService.dropOffPassengers(this.location.toString(), this);
			busStopService.pickUpPassengers(this);
			Thread.sleep(1);
		}
		for (String point : pointsVisited) {
			System.out.println(point); // testing purposes
		} 
		System.out.println("Current range: " +range);
		return "Route completed";
		 })
		 .subscribeOn(Schedulers.io())
		  .observeOn(Schedulers.single())
		  .subscribe(System.out::println, Throwable::printStackTrace);
	}

	@Override
	public void fuelUp() {
		// TODO Auto-generated method stub
	}
	
	private void simulateDrive(double timeToMove) throws InterruptedException {
		Thread.sleep((long) timeToMove * 1000);
		range = (long) (range - timeToMove);
	}

	@Override
	public void setRoute(DirectionsRoute route) {
		for (DirectionsLeg leg : route.legs) {
			for ( DirectionsStep step : leg.steps) {
				currentRoute.add(step);
			}
		}
	}

	public List<DirectionsStep> getRoute() {
		return currentRoute;
	}

	@Override
	public int getMaxPassengers() {
		return maxPassengers;
	}

	@Override
	public void dropPassenger(PassengerInterface passenger) {
		passengersOnBoard.remove(passenger);
	}

	@Override
	public String getLocation() {
		// TODO Auto-generated method stub
		return location.toString();
	}
	
	public void pickPassenger (PassengerInterface passenger) {
		passengersOnBoard.add(passenger);
		System.out.println("Picked up a passenger at: " + getLocation());
	}
	
}
