package interfaces;

import java.io.IOException;
import java.util.List;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;

public interface VehicleInterface {

	/**
	 * Sends the bus to the closest station to fuel up. The method uses the simplest possible route,
	 * disregarding passengers.
	 */
	public void visitClosestStationFuelUp() throws InterruptedException, ApiException, IOException;

	/**
	 * Sets the bus's intended route to the specified DirectionsRoute-object. Used mostly to move the bus
	 * to the wanted route before actual passenger-servicing begins.
	 * 
	 * @param route specifies the DirectionsRoute-object the vehicle's intended route to be set to
	 */
	public void setIntendedRoute(DirectionsRoute route);

	/**
	 * Returns a list of DirectionsSteps representing the currentRoute-variable of the bus. 
	 * 
	 * @return returns the LatLng-object representing the current location of the passenger.
	 */
	public List<DirectionsStep> getRoute();

	/**
	 * Returns the maximum amount of passengers the vehicle can take at any one time.
	 * 
	 * @return returns the int-value representing the maximum amount of passengers for the vehicle 
	 */
	public int getMaxPassengers();

	/**
	 * Returns a list of PassengerInterface-objects representing the passengers currently on-board. 
	 * 
	 * @return returns a list of PassengerInterface-objects representing the passengers currently on-board
	 */
	public List<PassengerInterface> getPassengersOnBoard();

	/**
	 * Returns the id of the vehicle. Id is used for a multitude of different operations.
	 * 
	 * @return returns the int-value representing the id of the vehicle
	 */
	public int getId();

	/**
	 * Remove the specified passenger from the vehicle.
	 * 
	 * @param passenger specifies the passenger to be removed from the vehicle
	 */
	public void dropPassenger(PassengerInterface passenger);

	/**
	 * Add the specified passenger on-board the vehicle.
	 * 
	 * @return returns a boolean-value, true if passenger successfully picked up
	 * @param passenger specifies the passenger to be removed from the vehicle
	 */
	public boolean pickPassenger(PassengerInterface passenger);
	
	/**
	 * Returns the LatLng-object for the coordinates of the vehicle.
	 * 
	 * @return returns the LatLng-object for the coordinates of the vehicle
	 */
	public LatLng getLocationCoords();

	/**
	 * Set boolean value whether to keep routing and driving the vehicle after finishing the current route.
	 * 
	 * @param driveOption boolean-value, set to true to keep driving after finishing current route
	 */
	void setKeepDrivingCurrentRoute(boolean driveOption);

	/**
	 * Set the vehicle to drive the intended route. Requires route to be set up beforehand.
	 */
	void driveRoute() throws InterruptedException, ApiException, IOException;
	
	/**
	 * Sets the specified String as the operating-type for the vehicle.
	 * 
	 * @param operatingType String-value the operating type of the vehicle should be set to
	 */
	void setOperatingType(String operatingType);

	/**
	 * Returns an int-value representing the current free seats of the vehicle.
	 * 
	 * @return returns the int-value representing the current free seats for the vehicle.
	 */
	int getFreeSeats();

	/**
	 * Reserve a seat for the specified passenger from the vehicle.
	 * 
	 * @param passenger PassengerInterface-object the seat should be reserved to.
	 */
	void addToReservedSeats(PassengerInterface passenger);

	/**
	 * Sets the DirectionsResult-object for the vehicle, contains useful information about the route.
	 * Method generally used with BusService - setRouteWaypoints or setRouteWaypointsOnDemand.
	 * 
	 * @param result specifies the DirectionsResult-object to be set for the vehicle.
	 */
	public void setRouteResults(DirectionsResult result);

}