package entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import services.*;
import utilities.*;
import interfaces.*;

public class Ez10 implements VehicleInterface {

	private String name = "EasyMile EZ10";
	private int id;
	private double range; // Operating distance left
	private double maxSpeed; // KM/h -- functions as the "Average Speed" of the bus
	private double maxSpeedMeters; // M/S
	private int turnAngle;
	private int width; // CM
	private int length; // CM
	private int height; // CM
	private int maxPassengers = 12;
	private String operatingFuel = "Electric";
	private String operatingType;
	private List<DirectionsStep> intendedRoute = new ArrayList<>();
	private List<DirectionsStep> currentRoute = new ArrayList<>();
	private List<PassengerInterface> passengersOnBoard = new ArrayList<>();
	private LatLng locationCoords;
	private boolean keepDrivingCurrentRoute;

	ArrayList<String> pointsVisited = new ArrayList<String>(); // test purposes

	public Ez10(int id) {
		this.id = id;
		this.maxSpeed = 1200;
		this.maxSpeedMeters = this.maxSpeed / 3.6;
		this.turnAngle = 35;
		this.width = 200; // CM
		this.length = 400; // CM
		this.height = 275; // CM
		this.maxPassengers = 12;
		// this.range = 14 * 60 * 60 * maxSpeedMeters;
		this.range = 5; // test purposes, the one above is the correct one
		this.operatingFuel = "Electric";
		this.operatingType = "Metro"; // Stop on every bus-stop
		this.keepDrivingCurrentRoute = true;
	}

	public int getId() {
		return id;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
		this.maxSpeedMeters = maxSpeed / 3.6;
	}

	public List<PassengerInterface> getPassengersOnBoard() {
		return passengersOnBoard;
	}

	/*
	 * For Metro and Bus-route -style routes, checks if range is enough to drive
	 * full route - if not, goes to station. 10% safety margin on route calculation.
	 * Currently drives to a route final destination and drives the same route back.
	 * Also assumes the current location of the bus is the first point of the route.
	 */
	@Override
	public void driveRouteMetro() throws InterruptedException, ApiException, IOException {
		FleetManager fleetManagement = FleetManager.getInstance();
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		currentRoute = intendedRoute;
		locationCoords = currentRoute.get(0).startLocation;
		if (calculateRouteLeft(intendedRoute.get(0)) < (range * 0.9)) {
			Flowable.fromCallable(() -> {
				for (int i = 0; i < currentRoute.size(); i++) {
					DirectionsStep currentStep = currentRoute.get(i);
					simulateDriveToNextStep(currentStep);
					busStopService.dropOffPassengers(this);
					busStopService.pickUpPassengers(this);
				}
				if (keepDrivingCurrentRoute) {
					setKeepDrivingCurrentRoute(false); // test - drive set route and back, then stop
					intendedRoute = getCurrentRouteInverted();
					driveRouteMetro();
				}
				return "Route completed";
			}).subscribeOn(Schedulers.io()).observeOn(Schedulers.single()).subscribe(System.out::println,
					Throwable::printStackTrace);
		} else {
			visitClosestStationFuelUp();
			driveRouteMetro();
		}
	}
	
	/*
	 * 	pointsVisited.add(currentStep.endLocation.toString());
	 * 	for (String point : pointsVisited) {
					System.out.println(point);
									}
	 * 
	 */

	private List<DirectionsStep> getCurrentRouteInverted() {
		List<DirectionsStep> tempRoute = new ArrayList<>();
		for (int x = currentRoute.size() - 1; x >= 0; x--) {
			tempRoute.add(currentRoute.get(x));
		}
		return tempRoute;
	}

	@Override
	public void setKeepDrivingCurrentRoute(boolean driveOption) {
		keepDrivingCurrentRoute = driveOption;
	}

	private void visitClosestStationFuelUp() throws ApiException, InterruptedException, IOException {
		System.out.println("Needs to charge, moving to station, charging and returning.");
		FleetManager fleetManagement = FleetManager.getInstance();
		BusService busService = fleetManagement.getBusServices().get("Espoo");
		Stack<DirectionsStep> stationRoute = new Stack<>();
		for (DirectionsLeg leg : busService.getRouteLatLon(this.id, this.getLocationCoords(),
				this.getClosestStation(intendedRoute.get(0)).getLocationCoords()).legs) {
			for (DirectionsStep step : leg.steps) {
				stationRoute.add(step);
			}
		}
		for (DirectionsStep step : stationRoute) {
			simulateDriveToNextStep(step);
		}
		fuelUp();
		int nOfSteps = stationRoute.size();
		for (int x = 0; x < nOfSteps; x++) {
			simulateDriveToNextStep(stationRoute.pop());
		}
		System.out.println("Fueled up and returned to origin");
	}

	@Override
	public void fuelUp() throws InterruptedException {
		Thread.sleep(5000); // actual charging takes about 7 hours
		this.range = 14 * 60 * 60 * maxSpeedMeters;
		;
		System.out.println("Vehicle fueled up");
	}

	private void simulateDriveToNextStep(DirectionsStep currentStep) throws InterruptedException {
		double distanceToTravel = currentStep.distance.inMeters;
		double timeToMove = distanceToTravel / maxSpeedMeters;
		this.locationCoords = currentStep.startLocation;
		System.out.println("Currently at: " + getLocationCoords() + ", next stop at "
				+ currentStep.endLocation.toString() + " moving will take " + timeToMove + " seconds");
		Thread.sleep((long) timeToMove * 1000);
		range = (long) (range - distanceToTravel);
		this.locationCoords = currentStep.endLocation;
	}

	private BusStopInterface getClosestStation(DirectionsStep currentStep) {
		FleetManager fleetManagement = FleetManager.getInstance();
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		DistanceUtils distanceUtils = DistanceUtils.getInstance();
		BusStopInterface closestStation = null;
		double distance = 1000000;
		for (BusStopInterface stop : busStopService.getAllStops()) {
			if (stop.getName() == "Station") {
				if (distanceUtils.getDistanceMeters(stop.getLocationCoords(),
						currentStep.endLocation.toString()) < distance) {
					closestStation = stop;
					distance = distanceUtils.getDistanceMeters(stop.getLocationCoords(), this.getLocationCoords());
				}
			}
		}
		return closestStation;
	}  

	private long calculateRouteLeft(DirectionsStep currentStep) {
		long routeLeft = 0;
		for (int i = currentRoute.indexOf(currentStep); i < currentRoute.size(); i++) {
			routeLeft = routeLeft + currentRoute.get(i).distance.inMeters;
		}
		return routeLeft;
	}

	@Override
	public void setIntendedRoute(DirectionsRoute route) {
		for (DirectionsLeg leg : route.legs) {
			for (DirectionsStep step : leg.steps) {
				intendedRoute.add(step);
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
	public String getLocationCoords() {
		return locationCoords.toString();
	}

	public void pickPassenger(PassengerInterface passenger) {
		passengersOnBoard.add(passenger);
		System.out.println("Picked up a passenger at: " + getLocationCoords());
	}
}