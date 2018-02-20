package entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.esotericsoftware.minlog.Log;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
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
	private double range; // Operating seconds left - calculated from 14h battery capacity
	private double maxSpeed; // KM/h -- functions as the "Average Speed" of the bus
	private double maxSpeedMeters; // M/S
	private int turnAngle;
	private int width; // CM
	private int length; // CM
	private int height; // CM
	private int maxPassengers;
	private String operatingFuel = "Electric";
	private String operatingType;
	private String busStatus;
	private long intendedRouteTimeStamp;
	private List<DirectionsStep> intendedRoute = new ArrayList<>();
	private List<DirectionsStep> currentRoute = new ArrayList<>();
	private List<PassengerInterface> passengersOnBoard = new ArrayList<>();
	private List<PassengerInterface> reservedSeats = new ArrayList<>();
	private List<PassengerInterface> nearbyPassengers = new ArrayList<>();
	private DirectionsResult directionsOverview;
	private LatLng locationCoords;
	private LatLng currentDestination;
	private boolean keepDrivingCurrentRoute;
	private double timeToCurrentDestination;
	private long destinationUpdateTimeStamp; // possibly temporary - needed for updating bus correctly on map
	DistanceUtils distanceUtils = DistanceUtils.getInstance();

	public Ez10(int id, String location) throws ApiException, InterruptedException, IOException {
		MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
		this.id = id;
		setMaxSpeed(200);
		this.turnAngle = 35;
		this.width = 200; // CM
		this.length = 400; // CM
		this.height = 275; // CM
		this.maxPassengers = 9;
		this.range = 14 * 60 * 60 * maxSpeedMeters;
		this.operatingFuel = "Electric";
		this.operatingType = "metro";
		this.busStatus = "power off";
		this.keepDrivingCurrentRoute = false;
		this.locationCoords = mapsUtils.getGeocodeLatLng(location);
	}

	@Override
	public void setOperatingType(String operatingType) {
		this.operatingType = operatingType;
	}

	public void setTimeToCurrentDestination(double timeToCurrentDestination) {
		this.timeToCurrentDestination = timeToCurrentDestination;
		destinationUpdateTimeStamp = System.currentTimeMillis();
	}

	@Override
	public void addToReservedSeats(PassengerInterface passenger) {
		reservedSeats.add(passenger);
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
	
	private String getBusStatus() {
		return busStatus;
	}

	/*
	 * For Metro and Bus-route -style routes, checks if range is enough to drive
	 * full route - if not, goes to station. 10% safety margin on route calculation.
	 * MESSY
	 */
	@Override
	public void driveRoute()  {
		try {
		currentRoute = intendedRoute;
		currentDestination = intendedRoute.get(0).endLocation;
		if (!currentRoute.get(0).startLocation.toString().equals(locationCoords.toString()) && calculateRouteLeft(currentRoute.get(0)) < (range * 0.9)) {
			Log.info("Driving to route");
				currentRouteToLocation(currentRoute.get(0).startLocation);
		}
		if (calculateRouteLeft(currentRoute.get(0)) < (range * 0.9)) {
			Flowable<String> source = Flowable.fromCallable(() -> {
				handlePassengers(currentRoute.get(0));
				for (int i = 0; i < currentRoute.size(); i++) {
					DirectionsStep currentStep = currentRoute.get(i);
					currentDestination = currentStep.endLocation;
					simulateDriveToNextStep(currentStep);
					handlePassengers(currentStep);
				}
				this.currentDestination = null;
				testKeepDrivingCurrentRoute();
				return "Route completed";
			});
			Flowable<String> runBackground = source.subscribeOn(Schedulers.io());
			Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());
			showForeground.subscribe(Log::info, Throwable::printStackTrace);
		} else {
			visitClosestStationFuelUp();
			driveRoute();
		}
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void currentRouteToLocation(LatLng locationCoords) throws ApiException, InterruptedException, IOException {
		FleetManager fleetManagement = FleetManager.getInstance();
		BusService busService = fleetManagement.getBusServices().get("Espoo");
		currentRoute = new ArrayList<>();
		DirectionsRoute simpleRoute = busService.getRouteSimple(this.getLocationCoords(), locationCoords);
		for (DirectionsLeg leg : simpleRoute.legs) {
			for (DirectionsStep step : leg.steps) {
				currentRoute.add(step);
			}
		}
		driveToRoute();
		Log.info("Reached route starting point");
		currentRoute = intendedRoute;
	}

	private void driveToRoute() throws ApiException, InterruptedException, IOException {
		for (int i = 0; i < currentRoute.size(); i++) {
			DirectionsStep currentStep = currentRoute.get(i);
			currentDestination = currentStep.endLocation;
			simulateDriveToNextStep(currentStep);
		}
		currentDestination = null;
	}

	private void handlePassengers(DirectionsStep currentStep) throws InterruptedException {
		FleetManager fleetManagement = FleetManager.getInstance();
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		if (operatingType.equals("metro")) {
			busStopService.dropOffPassengers(this);
			busStopService.pickUpPassengers(this);
		} else if (operatingType.equals("busRoute")
				&& (getBooleanPassengersNearby(currentStep) || getBooleanPassengerDestinationNearby(currentStep))) {
			busStopService.dropOffPassengers(this);
			busStopService.pickUpPassengers(this);
		} else if (operatingType.equals("onDemand")) {
			if (getBooleanPassengerDestinationNearby(currentStep)) {
				dropOffPassengersOnDemand();
			}
			if (getBooleanPassengersNearby(currentStep)) {
				pickUpNearbyPassengersOnDemand();
			}
			removeInvalidReservedSeats();
		}
	}

	private void testKeepDrivingCurrentRoute() throws InterruptedException, ApiException, IOException {
		if (keepDrivingCurrentRoute) {
			if (!operatingType.equals("onDemand")) {
				setKeepDrivingCurrentRoute(false); // test - drive set route and back, then stop
				intendedRoute = getCurrentRouteInverted();
				driveRoute();
			} else if (operatingType.equals("onDemand")) {
				FleetManager fleetManagement = FleetManager.getInstance();
				BusService busService = fleetManagement.getBusServices().get("Espoo");
				PassengerService passengerService = fleetManagement.getPassengerServices().get("Espoo");
				boolean driveRoute = false;
				for (PassengerInterface passenger : passengerService.getAllPassengers()) {
					if (passenger.getStatus().equals("waiting")) {
						driveRoute = true;
					} else {
						for (PassengerInterface passengerOnBoard : passengersOnBoard) {
							if (passengerOnBoard.getStatus().equals("on board")) {
								driveRoute = true;
							}
						}
					}
				}
				if (driveRoute) {
					busService.setRouteWaypointsOnDemand(this.getId(), this.locationCoords.toString(),
							this.currentRoute.get(0).startLocation.toString(), "Espoo");
					driveRoute();
				}
			}
		}
	}

	private void dropOffPassengersOnDemand() {
		List<PassengerInterface> droppingPassengers = new ArrayList<>();
		for (PassengerInterface passenger : passengersOnBoard) {
			if (distanceUtils.getDistanceMeters(this.locationCoords,
					passenger.getDestinationCoords()) < 50) {
				droppingPassengers.add(passenger);
			}
		}
		for (PassengerInterface passenger : droppingPassengers) {
			dropPassenger(passenger);
		}
	}

	private void pickUpNearbyPassengersOnDemand() {
		List<PassengerInterface> removingFromReservedSeats = new ArrayList<>();
		for (PassengerInterface passenger : nearbyPassengers) {
			for (PassengerInterface passengerSeatReserved : reservedSeats) {
				if (passenger == passengerSeatReserved) {
					removingFromReservedSeats.add(passengerSeatReserved);
					pickPassenger(passenger);
				}
			}
		}
		for (PassengerInterface passenger : removingFromReservedSeats) {
			nearbyPassengers.remove(passenger);
			reservedSeats.remove(passenger);
		}
		for (PassengerInterface passenger : nearbyPassengers) {
			pickPassenger(passenger);
		}
	}

	private boolean getBooleanPassengerDestinationNearby(DirectionsStep currentStep) {
		for (PassengerInterface passenger : passengersOnBoard) {
			if (distanceUtils.getDistanceMeters(this.getLocationCoords(),
					passenger.getDestinationCoords()) < 50) {
				return true;
			}
		}
		return false;
	}

	private void removeInvalidReservedSeats() {
		if (operatingType.equals("onDemand")) {
			List<PassengerInterface> removingFromReservedSeats = new ArrayList<>();
			for (PassengerInterface passenger : reservedSeats) {
				if (distanceUtils.getDistanceMeters(this.getLocationCoords(), passenger.getOriginCoords()) < 50
						&& !passenger.getStatus().equals("bus on route")) {
					removingFromReservedSeats.add(passenger);
				}
			}
			for (PassengerInterface passenger : removingFromReservedSeats) {
				reservedSeats.remove(passenger);
			}
		}
	}

	private boolean getBooleanPassengersNearby(DirectionsStep currentStep) {
		FleetManager fleetManagement = FleetManager.getInstance();
		PassengerService passengerService = fleetManagement.getPassengerServices().get("Espoo");
		boolean passangersNearbyBoolean = false;
		for (PassengerInterface passenger : passengerService.getAllPassengers()) {
			if ((passenger.getStatus().equals("waiting") || passenger.getStatus().equals("bus on route"))
					&& (distanceUtils.getDistanceMeters(this.getLocationCoords(),
							passenger.getCurrentCoords()) < 50)) {
				boolean currentRouteNearDestination = false;
				for (DirectionsStep step : currentRoute) {
					if (distanceUtils.getDistanceMeters(passenger.getDestinationCoords(),
							step.endLocation) < 50 || reservedSeats.contains(passenger)) {
						currentRouteNearDestination = true;
						passangersNearbyBoolean = true;
					}
				}
				if (currentRouteNearDestination) {
					nearbyPassengers.add(passenger);
				}
			}
		}
		return passangersNearbyBoolean;
	}

	private List<DirectionsStep> getCurrentRouteInverted() {
		List<DirectionsStep> tempRoute = new ArrayList<>();
		for (int x = currentRoute.size() - 1; x >= 0; x--) {
			LatLng newStart = currentRoute.get(x).endLocation;
			LatLng newEnd = currentRoute.get(x).startLocation;
			currentRoute.get(x).startLocation = newStart;
			currentRoute.get(x).endLocation = newEnd;
			tempRoute.add(currentRoute.get(x));
		}
		return tempRoute;
	}

	@Override
	public void setKeepDrivingCurrentRoute(boolean driveOption) {
		keepDrivingCurrentRoute = driveOption;
	}

	@Override
	public void visitClosestStationFuelUp() throws InterruptedException, ApiException, IOException {
		Log.info("Needs to charge, moving to station, charging and returning.");
		FleetManager fleetManagement = FleetManager.getInstance();
		BusService busService = fleetManagement.getBusServices().get("Espoo");
		Stack<DirectionsStep> stationRoute = new Stack<>();
		DirectionsLeg[] simpleRoute =  busService.getRouteSimple(this.getLocationCoords(),this.getClosestStop(intendedRoute.get(0), "Station").getLocationCoords()).legs;
		for (DirectionsLeg leg : simpleRoute) {
			for (DirectionsStep step : leg.steps) {
				stationRoute.add(step);
			}
		}
		for (DirectionsStep step : stationRoute) {
			simulateDriveToNextStep(step);
		}
		fuelUp();
		int nOfSteps = stationRoute.size();
		// Untested
		for (int x = 0; x < nOfSteps; x++) {
			LatLng tempStart = stationRoute.peek().endLocation;
			LatLng tempEnd = stationRoute.peek().startLocation;
			stationRoute.peek().startLocation = tempStart;
			stationRoute.peek().startLocation = tempEnd;
			simulateDriveToNextStep(stationRoute.pop());
		}
		Log.info("Fueled up and returned to origin");
	}
	
	private double fuelUp() throws InterruptedException {
		Thread.sleep(5000); // actual charging takes about 7 hours
		this.range = 14 * 60 * 60 * maxSpeedMeters;
		return this.range;
	}

	private void simulateDriveToNextStep(DirectionsStep currentStep) throws InterruptedException {
		this.locationCoords = currentStep.startLocation;
		double distanceToTravel = currentStep.distance.inMeters;
		double timeToMove = distanceToTravel / maxSpeedMeters; // seconds
		timeToCurrentDestination = currentStep.distance.inMeters / maxSpeedMeters; // seconds
		destinationUpdateTimeStamp = System.currentTimeMillis();
		nearbyPassengers = new ArrayList<>();
		Log.info("Bus id: " + this.getId() + ", currently at: " + getLocationCoords() + ", next stop at "
				+ currentStep.endLocation.toString() + " moving will take " + timeToMove + " seconds");
		Thread.sleep((long) timeToMove * 1000);
		range = (long) (range - distanceToTravel);
		this.locationCoords = currentStep.endLocation;
	}

	private BusStopInterface getClosestStop(DirectionsStep currentStep, String stopType) {
		FleetManager fleetManagement = FleetManager.getInstance();
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		BusStopInterface closestStop = null;
		double distance = 1000000;
		for (BusStopInterface stop : busStopService.getAllStops()) {
			if (stop.getName() == stopType) {
				if (distanceUtils.getDistanceMeters(stop.getLocationCoords(),
						currentStep.startLocation) < distance) {
					closestStop = stop;
					distance = distanceUtils.getDistanceMeters(stop.getLocationCoords(), this.getLocationCoords());
				}
			} else if (stop.getName() == stopType) {
				if (distanceUtils.getDistanceMeters(stop.getLocationCoords(),
						currentStep.startLocation) < distance) {
					closestStop = stop;
					distance = distanceUtils.getDistanceMeters(stop.getLocationCoords(), this.getLocationCoords());
				}
			}
		}
		return closestStop;
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
		intendedRouteTimeStamp = System.currentTimeMillis();
		intendedRoute = new ArrayList<>();
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
	public int getFreeSeats() {
		int freeSeats = maxPassengers - reservedSeats.size() - passengersOnBoard.size();
		return freeSeats;
	}

	@Override
	public LatLng getLocationCoords() {
		return locationCoords;
	}

	@Override
	public void dropPassenger(PassengerInterface passenger) {
		passengersOnBoard.remove(passenger);
		passenger.setStatus("delivered");
		Log.info("Dropped passanger id: " + passenger.getId());
	}

	@Override
	public boolean pickPassenger(PassengerInterface passenger) {
		if ((!passengersOnBoard.contains(passenger)) && (getFreeSeats() > 0) || reservedSeats.contains(passenger)) {
			passengersOnBoard.add(passenger);
			passenger.setCurrentCoords(null);
			passenger.setStatus("on board");
			Log.info("Picked passanger id: " + passenger.getId());
			return true;
		}
		return false;
	}

	@Override
	public void setRouteResults(DirectionsResult result) {
		this.directionsOverview = result;
	}
}