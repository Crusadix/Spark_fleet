package services.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.esotericsoftware.minlog.Log;
import com.google.maps.errors.ApiException;
import factories.*;
import interfaces.*;
import services.*;
import utilities.*;

public class BusStopServiceImpl implements BusStopService{

	DistanceUtils distanceUtils = DistanceUtils.getInstance();
	private List<BusStopInterface> stops = new ArrayList<>();
	StopFactory stopFactory = new StopFactory();

	@Override
	public List<BusStopInterface> getAllStops() {
		return stops;
	}

	@Override
	public BusStopInterface getStop(int stopId) {
		for (BusStopInterface tempStop : stops) {
			if (tempStop.getId() == stopId) {
				return tempStop;
			}
		}
		throw new IllegalArgumentException("Stop not found - BusStopService");
	}

	@Override
	public String[] buildWaypoints(String waypoints) {
		if (waypoints.length() < 2) {
			String[] coords = {getStop(Integer.parseInt(waypoints)).getLocationCoords().toString()};
			return coords;
		}
		String[] parts = waypoints.split(",");
		String[] coords = new String[parts.length];
		for (int y = 0; y < parts.length; y++) {
			for (int x = 0; x < stops.size(); x++) {
				if (Integer.parseInt(parts[y]) == stops.get(x).getId()) {
					coords[y] = (stops.get(x).getLocationCoords().toString());
				}
			}
		}
		return coords;
	}

	@Override
	public BusStopInterface createStation(String fuelType, String storageType, String location) throws ApiException, InterruptedException, IOException {
		BusStopInterface newStop = stopFactory.getStop(fuelType, storageType, location);
		stops.add(newStop);
		return newStop;
	}

	@Override
	public BusStopInterface createBusStop(String location) throws ApiException, InterruptedException, IOException {
		BusStopInterface newStop = stopFactory.getStop(location);
		stops.add(newStop);
		return newStop;
	}

	@Override
	public void addPassenger(int stopId, PassengerInterface passenger) {
		getStop(stopId).addPassenger(passenger);
	}

	/*
	 * Checks distance to all stops, then checks whether passenger destination is
	 * close to the stops close-by. 
	 */
	@Override
	public void dropOffPassengers(VehicleInterface vehicle) {
		for (int x = 0; x < (stops.size()); x++) {
			if (distanceUtils.getDistanceMeters(stops.get(x).getLocationCoords(), vehicle.getLocationCoords()) < 50) {
				Log.info("Arrived at " + vehicle.getLocationCoords());
				List<PassengerInterface> droppingPassangers = new ArrayList<>();
				for (PassengerInterface tempPassenger : vehicle.getPassengersOnBoard()) {
					if (distanceUtils.getDistanceMeters(tempPassenger.getDestinationCoords(), vehicle.getLocationCoords()) < 50) {
						droppingPassangers.add(tempPassenger);
					}
				}
				for (PassengerInterface tempPassenger : droppingPassangers) {
					vehicle.dropPassenger(tempPassenger);
				}
			}
		}
	}

	@Override
	public void pickUpPassengers(VehicleInterface vehicle) {
		for (BusStopInterface busStop : stops) {
			if (distanceUtils.getDistanceMeters(busStop.getLocationCoords(), vehicle.getLocationCoords()) < 50) {
				Log.info("Passengers taken onboard");
				List<PassengerInterface> pickingPassangers = new ArrayList<>();
				for (int y = 0; y < busStop.getPassengersWaiting().size();y++) {
					for (BusStopInterface endBusStop : stops) {
						if (distanceUtils.getDistanceMeters(busStop.getPassengersWaiting().get(y).getDestinationCoords(), endBusStop.getLocationCoords()) < 50) {
							pickingPassangers.add(busStop.getPassengersWaiting().get(y));
						}
					}
				}
				for (PassengerInterface passenger : pickingPassangers) {
					vehicle.pickPassenger(passenger);
					busStop.removePassenger(passenger);
				}
			}
		}
	}
}
