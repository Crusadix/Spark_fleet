package interfaces;

import java.util.ArrayList;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

public interface VehicleInterface {

	public void moveTo(String location);

	public void fuelUp();

	public void pickPassenger(int passengersAmount);

	public void setRoute(DirectionsRoute route);

	public ArrayList<DirectionsStep> getRoute();

}
