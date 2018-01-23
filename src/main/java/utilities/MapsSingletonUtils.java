package utilities;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class MapsSingletonUtils {

	private static MapsSingletonUtils mapsUtils = new MapsSingletonUtils();
	private static GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyCnKfFi02VENIr0EXMND16fdBYSjxfncHA").build();
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private MapsSingletonUtils() {
	}

	public static MapsSingletonUtils getInstance() {
		return mapsUtils;
	}

	public static GeoApiContext getGeoApiContext() {
		return context;
	}

	public static Gson getGsonBuilder() {
		return gson;
	}

	public String getGeocode(String location) throws ApiException, InterruptedException, IOException {
		GeocodingResult[] results = GeocodingApi.geocode(getGeoApiContext(), location).await();
		return Double.toString(results[0].geometry.location.lat) + ","
				+ Double.toString(results[0].geometry.location.lng);
	}
	
	public LatLng getGeocodeLatLng(String location) throws ApiException, InterruptedException, IOException {
		GeocodingResult[] results = GeocodingApi.geocode(getGeoApiContext(), location).await();
		return results[0].geometry.location;
	}
}
