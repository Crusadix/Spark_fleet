package services;

import java.io.IOException;
import java.util.List;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import interfaces.*;

public interface BusService {

	/**
	 * Return a simple route between the origin and destination parameters, function
	 * used to move the bus from a location to another without passengers and to
	 * build basis for on-demand route building.
	 * 
	 * @return returns a com.google.maps.model.DirectionsRoute -object, detailing
	 *         the specifics of the route. Can return an empty object.
	 * @param origin is the starting location for the route, often the initial location
	 *            of the bus
	 * @param destination is the ending location the bus should reach
	 */
	DirectionsRoute getRouteSimple(LatLng origin, LatLng destination)
			throws ApiException, InterruptedException, IOException;

	/**
	 * Return a specifics for a route between the origin and destination parameters
	 * with additional details, function used to create a on-demand route matching
	 * the current passengers waiting to be picked up along the way. Passengers are
	 * added to the route with a radius of 1500 meters from each step in the route.
	 * 
	 * Also sets the route to be used for the specified busId.
	 * 
	 * @return returns a com.google.maps.model.DirectionsResult -object, detailing
	 *         the specifics of the route and additional information. Can return an
	 *         empty object.
	 * @param busId specifies the ID of the bus you wish to set the route for
	 * @param origin is the starting location for the route, often the initial location
	 *            of the bus
	 * @param destination  is the ending location the bus should reach
	 * @param zone specifies fleet-control area
	 */
	DirectionsResult setRouteWaypointsOnDemand(int busId, String origin, String destination, String zone)
			throws ApiException, InterruptedException, IOException;

	/**
	 * Return a specifics for a route between the origin and destination parameters
	 * with additional details, function used to create either a bus-route, or metro
	 * -type route for a vehicle.
	 * 
	 * Also sets the route to be used for the specified busId.
	 * 
	 * @return returns a com.google.maps.model.DirectionsResult -object, detailing
	 *         the specifics of the route and additional information. Can return an
	 *         empty object.
	 * @param busId specifies the ID of the bus you wish to set the route for
	 * @param origin is the starting location for the route, often the initial location
	 *            of the bus
	 * @param destination is the ending location the bus should reach
	 * @param waypoints details the bus-stops or stations to be added to the route
	 */
	DirectionsResult setRouteWaypoints(int busId, String origin, String destination, String waypoints, String zone)
			throws ApiException, InterruptedException, IOException;

	/**
	 * Creates a new instance of Ez10 and returns a VehicleInterface -object
	 * detailing the specifics of the created bus.
	 * 
	 * @return returns a VehicleInterface -object detailing the specifics of the
	 *         created bus
	 * @param location specifies the current location for the new bus
	 */
	VehicleInterface createBus(String location) throws ApiException, InterruptedException, IOException;

	/**
	 * Returns true if the bus successfully starts driving on route. Requires the
	 * route to be set up first.
	 * 
	 * @return returns true if the bus successfully started driving on route
	 * @param busId used to find a specific bus
	 * @param operationType details the desired operation-type for the bus
	 */
	boolean driveCurrentRoute(int busId, String operationType) throws ApiException, InterruptedException, IOException;

	/**
	 * Returns a VehicleInterface -object detailing the specifics of the bus.
	 * 
	 * @return returns a VehicleInterface -object detailing the specifics of the bus
	 * @param busId used to find a specific bus
	 */
	VehicleInterface getBus(int busId);

	/**
	 * Returns a list of the current VehicleInterface -objects.
	 * 
	 * @return returns a list of the current VehicleInterface -objects.
	 */
	List<VehicleInterface> getAllBuses();

}