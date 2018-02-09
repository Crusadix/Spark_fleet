package services;

import java.io.IOException;
import java.util.List;
import com.google.maps.errors.ApiException;
import interfaces.*;

public interface BusStopService {

	void addPassenger(int stopId, PassengerInterface passenger);

	BusStopInterface createStation(String fuelType, String storageType, String location) throws ApiException, InterruptedException, IOException;

	BusStopInterface createBusStop(String location) throws ApiException, InterruptedException, IOException;

	List<BusStopInterface> getAllStops();

	BusStopInterface getStop(int stopId);

	String[] buildWaypoints(String waypoints);

	void dropOffPassengers(VehicleInterface vehicle) throws InterruptedException;

	void pickUpPassengers(VehicleInterface vehicle) throws InterruptedException;

}