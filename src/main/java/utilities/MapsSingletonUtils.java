package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;

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

}
