package services;

import java.io.IOException;
import java.util.List;
import com.google.maps.errors.ApiException;
import interfaces.*;

public interface PassengerService {

	/**
	 * Builds a route based on the route between origin and destination -parameters
	 * and the current passengers, picking up passengers within 1500 meter-radius
	 * for each step in the route for the vehicle specified. Does not include
	 * passengers in the route if bus cannot take more passengers.
	 * 
	 * @return returns a PassengerInterface -object detailing the specifics of the
	 *         passenger
	 * @param passengerId used to find a specific passenger
	 */
	String[] buildPassengerWaypoints(VehicleInterface vehicle, String origin, String destination)
			throws ApiException, InterruptedException, IOException;

	/**
	 * Returns a list of the current existing PassengerInterface -objects.
	 * 
	 * @return returns a list of the current PassengerInterface -objects.
	 */
	List<PassengerInterface> getAllPassengers();

	/**
	 * Returns a PassengerInterface -object detailing the specifics of the
	 * passenger.
	 * 
	 * @return returns a PassengerInterface -object detailing the specifics of the
	 *         passenger
	 * @param passengerId used to find a specific passenger
	 */
	PassengerInterface getPassenger(int passengerId);

	/**
	 * Creates new passenger and returns the specifics of the passenger if
	 * successful.
	 * 
	 * @return returns a PassengerInterface -object detailing the specifics of the
	 *         created passenger
	 * @param origin is the starting location for the passenger
	 * @param destination is the final destination for the passenger
	 */
	PassengerInterface createPassenger(String origin, String destination)
			throws ApiException, InterruptedException, IOException;

	/**
	 * The specified passenger is added to the bus-stop/station with a corresponding
	 * stopId. Returns a PassengerInterface -object detailing the specifics of the
	 * passenger.
	 * 
	 * @return returns a PassengerInterface -object detailing the specifics of the
	 *         passenger.
	 * @param passengerId used to find a specific passenger
	 * @param stopId  used to find a specific stop
	 */
	PassengerInterface moveToBusStop(int passengerId, int stopId);

}