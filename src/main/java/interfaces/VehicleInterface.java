package interfaces;

import java.util.List;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

public interface VehicleInterface {

	public void driveRoute() throws InterruptedException;

	public void fuelUp();

	public void setRoute(DirectionsRoute route);

	public List<DirectionsStep> getRoute();

	public int getMaxPassengers();

	public List<PassengerInterface> getPassengersOnBoard();

	public int getId();

	public void dropPassenger(PassengerInterface passenger);

	public String getLocation();

	public void pickPassenger(PassengerInterface passenger);

}

//Vehicle ez10 = BusFactory.createEasymileBus();