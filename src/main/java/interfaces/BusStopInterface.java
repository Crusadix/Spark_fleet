package interfaces;

import java.util.List;

public interface BusStopInterface {

	public List<PassengerInterface> getPassengersWaiting();

	public void addPassenger(PassengerInterface passenger);
	
	public void removePassenger(PassengerInterface passenger);

	public String getLocationCoords();

	public int getId();
	
	public void fuelVehicle (VehicleInterface vehicle);
	
	public String getName();

	public String getLocation();
}
