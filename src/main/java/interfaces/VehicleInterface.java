package interfaces;

public interface VehicleInterface {


	public long batteryLevel = 100;
	public long range = 0;
	public int maxSpeed = 20;
	public long turnAngle = 35;
	public int width = 200; //CM
	public int length = 2500; //cm
	public int maxPassengers = 12;
	public int currentPassengers = 0;
	public String operatingFuel = "Electric";
	public String operatingType = "onDemand";
	
	public String setBatteryLevel(String batteryLevel);
	
	public long getBatteryLevel();
}

	

