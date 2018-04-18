package entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.esotericsoftware.minlog.Log;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;
import enums.BusStatus;
import enums.PassengerStatus;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import services.*;
import utilities.*;
import interfaces.*;

public class Ez10 extends AbstractBus {
	
	public Ez10(int id, String location) throws ApiException, InterruptedException, IOException {
		MapsSingletonUtils mapsUtils = MapsSingletonUtils.getInstance();
		this.id = id;
		this.name = "EZ10";
		setMaxSpeed(40);
		this.turnAngle = 35;
		this.width = 200; // CM
		this.length = 400; // CM
		this.height = 275; // CM
		this.maxPassengers = 9;
		this.range = 14 * 60 * 60 * maxSpeedMeters;
		this.operatingFuel = "Electric";
		this.operatingType = "metro";
		this.busStatus = BusStatus.powerOff;
		this.keepDrivingCurrentRoute = true;
		this.locationCoords = mapsUtils.getGeocodeLatLng(location);
	}

}