package entities;

import java.io.IOException;

import com.google.maps.errors.ApiException;

import enums.BusStatus;
import utilities.MapsSingletonUtils;

public class Navya extends AbstractBus {
	
	public Navya(int id, String location) throws ApiException, InterruptedException, IOException {
		MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
		this.id = id;
		this.name = "Navya Autonom Shuttle";
		setMaxSpeed(40);
		this.turnAngle = 40; // random value
		this.width = 211; // CM
		this.length = 475; // CM
		this.height = 265; // CM
		this.maxPassengers = 15;
		this.range = 14 * 60 * 60 * maxSpeedMeters;
		this.operatingFuel = "Electric";
		this.operatingType = "metro";
		this.busStatus = BusStatus.powerOff;
		this.keepDrivingCurrentRoute = true;
		this.locationCoords = mapsUtils.getGeocodeLatLng(location);
	}

}
