package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.GeocodingResult;

import entities.Ez10;
import interfaces.VehicleInterface;

public class BusService {

	GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyCnKfFi02VENIr0EXMND16fdBYSjxfncHA").build();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private Map<Integer, VehicleInterface> buses = new HashMap<>();

	public List<VehicleInterface> getAllBuses() {
		return new ArrayList<>(buses.values());
	}

	public ArrayList<DirectionsStep> getRoute(int id) {
		return buses.get(id).getRoute();
	}

	public VehicleInterface getBus(int id) {
		return buses.get(id);
	}

	public VehicleInterface createBus(int id) {
		VehicleInterface newBus = new Ez10(id);
		buses.put(id, newBus);
		return newBus;
	}
	
	public String driveCurrentRoute(int id) throws ApiException, InterruptedException, IOException {
		
		/*GeocodingResult[] results = GeocodingApi.geocode(context, location).await();
		buses.get(id).moveTo(gson.toJson(results[0].addressComponents)); */
		
		return buses.get(id).moveTo();
		 
	}

	public String getGeocode(String location) throws ApiException, InterruptedException, IOException {
		GeocodingResult[] results = GeocodingApi.geocode(context, location).await();
		return (gson.toJson(results[0].addressComponents));
	}

	public String setBusRoute(int id, String origin, String destination)
			throws ApiException, InterruptedException, IOException {
		DirectionsResult result = DirectionsApi.getDirections(context, origin, destination).await();
		buses.get(id).setRoute(result.routes[0]);
		return gson.toJson(result);
	}

}
