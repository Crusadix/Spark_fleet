package utilities;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

public class MapsSingletonUtils {

	private static MapsSingletonUtils mapsUtils = new MapsSingletonUtils();
	private static GeoApiContext context;
	private static Gson gson;

	private MapsSingletonUtils() {
		this.context = new GeoApiContext.Builder().apiKey("AIzaSyCnKfFi02VENIr0EXMND16fdBYSjxfncHA").build();
		this.gson = new GsonBuilder().setPrettyPrinting().create();
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
		GeocodingResult[] results = GeocodingApi.geocode(mapsUtils.getGeoApiContext(), location).await();
		return Double.toString(results[0].geometry.location.lat) + ","
				+ Double.toString(results[0].geometry.location.lng);
	}

}
