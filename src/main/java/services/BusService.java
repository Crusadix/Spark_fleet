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
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;

import entities.Ez10;
import interfaces.VehicleInterface;

public class BusService {

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

	public String setBusRoute(int id, String origin, String destination)
			throws ApiException, InterruptedException, IOException {

		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyCnKfFi02VENIr0EXMND16fdBYSjxfncHA").build();
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// GeocodingResult[] results = GeocodingApi.geocode(context,"Siltakuja 2,
		// espoo").await();
		// System.out.println(gson.toJson(results[0].addressComponents));
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		DirectionsResult result = DirectionsApi.getDirections(context, origin, destination).await();
		
		VehicleInterface tempBus = buses.get(id);
		tempBus.setRoute(result.routes[0]);
		
		
		return gson.toJson(result);

		// "Siltakuja 2, Espoo", "Sello, Espoo"
	}

}
