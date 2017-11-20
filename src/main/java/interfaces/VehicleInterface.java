package interfaces;

import java.util.ArrayList;
import java.util.List;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

public interface VehicleInterface {

	public String moveTo() throws InterruptedException;

	public void fuelUp();

	public void setRoute(DirectionsRoute route);

	public ArrayList<DirectionsStep> getRoute();

	public int getMaxPassengers();

	public List<PassengerInterface> getPassengersOnBoard();

	void pickPassengers(BusStopInterface busStopInterface);

}
