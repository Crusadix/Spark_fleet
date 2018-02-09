package services;

import java.io.IOException;
import java.util.List;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import interfaces.*;

public interface BusService {

	DirectionsRoute getRouteSimple(String origin, String destination) throws ApiException, InterruptedException, IOException;

	DirectionsResult setRouteWaypointsOnDemand(int busId, String origin, String destination, String zone) throws ApiException, InterruptedException, IOException;

	DirectionsResult setRouteWaypoints(int busId, String origin, String destination, String waypoints, String zone) throws ApiException, InterruptedException, IOException;

	VehicleInterface createBus(String location) throws ApiException, InterruptedException, IOException;

	boolean driveCurrentRoute(int busId, String operationType) throws ApiException, InterruptedException, IOException;

	VehicleInterface getBus(int busId);

	List<VehicleInterface> getAllBuses();

}
