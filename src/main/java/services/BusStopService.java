package services;

import java.io.IOException;
import java.util.List;
import com.google.maps.errors.ApiException;
import interfaces.*;

public interface BusStopService {

	/**
	 * Adds passenger to a bus-stop's list of current passengers
	 * 
	 * @param stopId specifies the ID of the stop the passenger should be added to
	 * @param passenger-object you wish to add to the stop
	 */
	void addPassenger(int stopId, PassengerInterface passenger);

	/**
	 * Creates new station and returns the specifics of the BusStopInterface if
	 * successful.
	 * 
	 * @return returns a BusStopInterface -object detailing the specifics of the
	 *         created station
	 * @param fuelType details what the vehicle uses as fuel
	 * @param storageType details the specifics of storage-possibilities for the vehicle
	 * @param location specifies where the bus-stop should be created
	 */
	BusStopInterface createStation(String fuelType, String storageType, String location)
			throws ApiException, InterruptedException, IOException;

	/**
	 * Creates new bus-stop and returns the specifics of the BusStopInterface if
	 * successful.
	 * 
	 * @return returns a BusStopInterface -object detailing the specifics of the
	 *         created station
	 * @param location specifies where the bus-stop should be created
	 */
	BusStopInterface createBusStop(String location) throws ApiException, InterruptedException, IOException;

	/**
	 * Returns a list of the currently existing BusStopInterface -objects.
	 * 
	 * @return returns a list of the current BusStopInterface -objects.
	 */
	List<BusStopInterface> getAllStops();

	/**
	 * Returns a BusStopInterface -object detailing the specifics of the stop.
	 * 
	 * @return returns a BusStopInterface -object detailing the specifics of the
	 *         stop
	 * @param stopId used to find a specific stop
	 */
	BusStopInterface getStop(int stopId);

	/**
	 * Returns a list of stopIds as strings, the function is used for building metro
	 * and bus-type -operation as they rely on stops.
	 * 
	 * @return returns a list of stopIds as strings
	 * @param waypoints are the stopIds of the stops desired for the route
	 */
	String[] buildWaypoints(String waypoints);

	/**
	 * Function used to drop passengers off at their destination. Checks whether the
	 * vehicle in question is in proximity of the destination of any current
	 * passengers on-board and removes the ones that have reached their destination.
	 * 
	 * @param vehicle-object dropping-off passengers
	 */
	void dropOffPassengers(VehicleInterface vehicle) throws InterruptedException;

	/**
	 * Function used to pick-up passengers if their destination is within the bus's
	 * route. Checks whether the vehicle in question will be in proximity of the
	 * destination of any potential passengers nearby and adds them on-board the
	 * bus.
	 * 
	 * @param vehicle-object picking-up passengers
	 */
	void pickUpPassengers(VehicleInterface vehicle) throws InterruptedException;

}