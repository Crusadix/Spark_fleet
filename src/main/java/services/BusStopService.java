package services;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import com.google.maps.errors.ApiException;
import utilities.MapsSingletonUtils;
import utilities.DistanceUtils;
import entities.BusStop;
import entities.Ez10;
import entities.Station;
import interfaces.BusStopInterface;
import interfaces.PassengerInterface;
import interfaces.VehicleInterface;

public class BusStopService {

	MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
	DistanceUtils distanceUtils = DistanceUtils.getInstance();
	private List<BusStopInterface> stops = new ArrayList<>();

	private static int stopId = 0;

	public int genId() {
		BusStopService.stopId++;
		return stopId;
	}

	public List<BusStopInterface> getAllStops() {
		return stops;
	}

	public BusStopInterface getStop(int id) {
		for (BusStopInterface temp : stops) {
			if (temp.getId() == id) {
				return temp;
			}
		}
		System.out.println("Stop not found - BusStopService");
		return null;
	}

	/*
	 * Get coordinates for ALL the currently existing stops!
	 */
	public String[] buildWaypoints() {
		String[] coords = new String[stops.size()];
		for (int x = 0; x < (stops.size()); x++) {
			coords[x] = (stops.get(x).getLocationCoords());
		}
		return coords;
	}

	public BusStopInterface createStation(String fuelType, String storageType, String location) throws ApiException, InterruptedException, IOException {
		BusStopInterface newStation = new Station(genId(), fuelType, storageType, location, mapsUtils.getGeocode(location));
		stops.add(newStation);
		return newStation;
	}

	public BusStopInterface createBusStop(String location) throws ApiException, InterruptedException, IOException {
		BusStopInterface newStop = new BusStop(genId(), location, mapsUtils.getGeocode(location));
		stops.add(newStop);
		return newStop;
	}

	public void addPassenger(int stopId, PassengerInterface passenger) {
		getStop(stopId).addPassenger(passenger);
	}

	/*
	 * Checks distance to every current bus-stop, if close picks up passengers.
	 * coords is the string value for the lat,lon of the bus stop in question!
	 */

	/*
	 * Checks distance to all stops, then checks whether passenger destination is
	 * close to the stops close-by. Unloads passengers one by one. coords is the
	 * string value for the lat,lon of the bus stop in question!
	 */
	public void dropOffPassengers(String coords, VehicleInterface vehicle) {
		for (int x = 0; x < (stops.size()); x++) {
			if (distanceUtils.getDistanceMeters(stops.get(x).getLocationCoords(), coords) < 50) {
				List<PassengerInterface> droppingPassangers = new ArrayList<>();
				for (PassengerInterface tempPassenger : vehicle.getPassengersOnBoard()) {
					if (distanceUtils.getDistanceMeters(tempPassenger.getDestinationCoords(), coords) < 50) {
						droppingPassangers.add(tempPassenger);
					}
				}
				for (PassengerInterface tempPassenger : droppingPassangers) {
					vehicle.dropPassenger(tempPassenger);
					tempPassenger.setStatus("Delivered");
					System.out.println("Dropped passanger id: " + tempPassenger.getId());
				}
			}
		}
	}

	public void pickUpPassengers(VehicleInterface vehicle) {
		for (int x = 0; x < (stops.size()); x++) {
			if (distanceUtils.getDistanceMeters(stops.get(x).getLocationCoords(), vehicle.getLocation()) < 50) {
				for (int y = 0; y < stops.get(x).getPassengersWaiting().size();y++) {
					vehicle.pickPassenger(stops.get(x).getPassengersWaiting().get(y));
					stops.get(x).getPassengersWaiting().remove(y);
				}
			}
		}
	}
}
