package interfaces;

import java.util.List;
import com.google.maps.model.LatLng;

public interface BusStopInterface {

	/**
	 * Returns a list of PassengerInterface-objects detailing the specifics of the
	 * passengers currently waiting to be picked up by a bus.
	 * 
	 * @return returns a list of PassengerInterface-objects detailing the specifics
	 *         of the passengers currently waiting to be picked up by a bus
	 */
	public List<PassengerInterface> getPassengersWaiting();

	/**
	 * Add a specified passenger to the stop.
	 * 
	 * @param passenger specifies the passenger to be added to the stop
	 */
	public void addPassenger(PassengerInterface passenger);

	/**
	 * Remove the specified passenger from the stop.
	 * 
	 * @param passenger specifies the passenger to be removed from the stop
	 */
	public void removePassenger(PassengerInterface passenger);

	/**
	 * Returns the LatLng-object for the coordinates of the stop.
	 * 
	 * @return returns the LatLng-object for the coordinates of the stop
	 */
	public LatLng getLocationCoords();

	/**
	 * Returns the id of the stop. Id is used for a multitude of different operations.
	 * 
	 * @return returns the int-value representing the id of the stop
	 */
	public int getId();

	/**
	 * Restore the fuel-amount of the specified VehicleInterface-object to maximum.
	 * 
	 * @param passenger specifies the vehicle to be fueled.
	 */
	public void fuelVehicle(VehicleInterface vehicle);

	/**
	 * Returns the name of the stop. String value. 
	 * 
	 * @return returns the String value representing the name of the stop.
	 */
	public String getName();

	/**
	 * Returns location of the stop as a String value (name of the place, for example: "Rajamäentie, espoo"). 
	 * 
	 * @return returns the String value representing the location of the stop.
	 */
	public String getLocation();
}
