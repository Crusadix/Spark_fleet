package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

import interfaces.VehicleInterface;

public class Ez10 implements VehicleInterface {

	private String name = "EasyMile EZ10";
	private int id;
	private long batteryLevel;
	private long range;
	private int maxSpeed = 20; // KM/h
	private long turnAngle;
	private int width; // CM
	private int length; // CM
	private int maxPassengers = 12;
	private int currentPassengers;
	private String operatingFuel = "Electric";
	private String operatingType;
	public Map<Integer, DirectionsStep> currentRoute = new HashMap<>();
	
	

	public Ez10(int ezId) {
		this.id = ezId;
		this.batteryLevel = 100;
		this.range = 0;
		this.maxSpeed = 20;
		this.turnAngle = 35;
		this.width = 200; // CM
		this.length = 250; // CM
		this.maxPassengers = 12;
		this.currentPassengers = 0;
		this.operatingFuel = "Electric";
		this.operatingType = "onDemand";
	}

	public int getBusId() {
		return id;
	}

	public void setBusId(int ezId) {
		this.id = ezId;
	}

	public long getBatteryLevel() {
		return batteryLevel;
	}

	public String setBatteryLevel(String batteryLevel) {
		this.batteryLevel = Integer.valueOf(batteryLevel);
		return batteryLevel;
	}

	public long getRange() {
		return range;
	}

	public void setRange(long range) {
		this.range = range;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public long getTurnAngle() {
		return turnAngle;
	}

	public void setTurnAngle(long turnAngle) {
		this.turnAngle = turnAngle;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getMaxPassengers() {
		return maxPassengers;
	}

	public void setMaxPassengers(int maxPassengers) {
		this.maxPassengers = maxPassengers;
	}

	public int getCurrentPassengers() {
		return currentPassengers;
	}

	public void setCurrentPassengers(int currentPassengers) {
		this.currentPassengers = currentPassengers;
	}

	public String getOperatingFuel() {
		return operatingFuel;
	}

	public void setOperatingFuel(String operatingFuel) {
		this.operatingFuel = operatingFuel;
	}

	public String getOperatingType() {
		return operatingType;
	}

	public void setOperatingType(String operatingType) {
		this.operatingType = operatingType;
	}

	@Override
	public void moveTo(String location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fuelUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pickPassenger(int passengersAmount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRoute(DirectionsRoute route) {
		
		for (int i = 0; i < route.legs[0].steps.length; i++) {
		    DirectionsStep step = route.legs[0].steps[i];
		    currentRoute.put(i,step);
		}
	}
	
	public ArrayList<DirectionsStep> getRoute() {
		return new ArrayList<>(currentRoute.values());
	}

}
