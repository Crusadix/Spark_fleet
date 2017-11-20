package interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public interface BusStopInterface {

	public Stack<PassengerInterface> getPassengersWaiting();

	public void addPassenger(PassengerInterface passenger);

	public PassengerInterface pickUpPassenger();

	public String getLocationCoords();

}
