package interfaces;

import java.util.List;

public interface BusStopInterface {

	public List<PassengerInterface> getPassengersWaiting();

	public void addPassenger(PassengerInterface passenger);

	public String getLocationCoords();

	public int getId();

	
}
