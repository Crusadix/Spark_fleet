package interfaces;

import java.util.List;
import java.util.Stack;

public interface BusStopInterface {

	public List<PassengerInterface> getPassengersWaiting();

	public void addPassenger(PassengerInterface passenger);

	public String getLocationCoords();

	public int getId();

	public PassengerInterface pickUpPassenger(PassengerInterface passenger); 
	
}
