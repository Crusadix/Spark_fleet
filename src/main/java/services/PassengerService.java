package services;

import java.io.IOException;
import java.util.List;
import com.google.maps.errors.ApiException;
import interfaces.*;

public interface PassengerService {

	String[] buildPassengerWaypoints(VehicleInterface vehicle, String locationCoords, String origin,
			String destination) throws ApiException, InterruptedException, IOException;

	List<PassengerInterface> getAllPassengers();
	
	PassengerInterface getPassenger(int passengerId);

	PassengerInterface createPassenger(String origin, String destination) throws ApiException, InterruptedException, IOException;

	PassengerInterface moveToBusStop(int passengerId, int stopId);

}
