package interfaces;

import java.io.IOException;
import java.util.List;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

public interface VehicleInterface {

	public void fuelUp() throws InterruptedException;

	public void setIntendedRoute(DirectionsRoute route);

	public List<DirectionsStep> getRoute();

	public int getMaxPassengers();

	public List<PassengerInterface> getPassengersOnBoard();

	public int getId();

	public void dropPassenger(PassengerInterface passenger);

	public String getLocationCoords();

	public void pickPassenger(PassengerInterface passenger);

	void setKeepDrivingCurrentRoute(boolean driveOption);
	
	void setTimeToCurrentDestination(double timeToCurrentDestination);

	void driveRoute() throws InterruptedException, ApiException, IOException;
	
	void setOperatingType(String operatingType);

}

//Vehicle ez10 = BusFactory.createEasymileBus();