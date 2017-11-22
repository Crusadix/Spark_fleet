package entities;

import java.io.IOException;
import java.util.List;

import com.google.maps.errors.ApiException;

import interfaces.BusStopInterface;
import interfaces.PassengerInterface;
import services.BusStopService;
import utilities.DistanceUtils;
import utilities.FleetManager;
import utilities.MapsSingletonUtils;

public class RegularPassenger implements PassengerInterface {
	
	private int id;
	private String origin;
	private String destination;
	private String originCoords;
	private String destinationCoords;

	public RegularPassenger(int id, String origin, String destination) throws ApiException, InterruptedException, IOException {
		this.id = id;
		this.origin = origin;
		this.destination = destination;

		FleetManager fleetManagement = FleetManager.getInstance();
		MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
		DistanceUtils distanceUtils = DistanceUtils.getInstance();
		BusStopService busStopService = fleetManagement.getBusStopServices().get("Espoo");
		
		this.originCoords = mapsUtils.getGeocode(origin);
		this.destinationCoords = mapsUtils.getGeocode(destination);
		
			for (int x = 0; x < (busStopService.getAllStops().size()); x++) {
				if (distanceUtils.getDistanceMeters(busStopService.getAllStops().get(x).getLocationCoords(), originCoords) < 50) {
					busStopService.addPassenger(busStopService.getAllStops().get(x).getId(), this);
				}
			}
	}

	@Override
	public int getId() {
		return id;
	}

	public String getDestinationCoords() {
		return destinationCoords;
	}
}
