package entities;
import interfaces.VehicleInterface;

public class Ez10 implements VehicleInterface{

	private int ezId;
	private long batteryLevel;
	private long range;
	private int maxSpeed;
	private long turnAngle;
	private int width; // CM
	private int length; // cm
	private int maxPassengers;
	private int currentPassengers;
	private String operatingFuel;
	private String operatingType;

	public Ez10(int ezId) {
		this.ezId = ezId;
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

	public Ez10(long batteryLevel, long range, int maxSpeed, long turnAngle, int width, int length, int maxPassengers,
			int currentPassengers, String operatingFuel, String operatingType) {
		this.batteryLevel = batteryLevel;
		this.range = range;
		this.maxSpeed = maxSpeed;
		this.turnAngle = turnAngle;
		this.width = width;
		this.length = length;
		this.maxPassengers = maxPassengers;
		this.currentPassengers = currentPassengers;
		this.operatingFuel = operatingFuel;
		this.operatingType = operatingType;
	}
	
	public int getBusId() {
		return ezId;
	}
	
	public void setBusId(int ezId) {
		this.ezId = ezId;
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

}

