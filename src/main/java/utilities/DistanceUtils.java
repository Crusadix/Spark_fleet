package utilities;

import org.gavaghan.geodesy.*;

import com.google.maps.model.LatLng;

public class DistanceUtils {

	private static DistanceUtils distanceUtils = new DistanceUtils();

	private DistanceUtils() {
	}

	public static DistanceUtils getInstance() {
		return distanceUtils;
	}

	public double getDistanceMeters(LatLng coordsA, LatLng coordsB) {
		double latA = coordsA.lat;
		double lonA = coordsA.lng;
		double latB = coordsB.lat;
		double lonB = coordsB.lng;
		GeodeticCalculator geoCalc = new GeodeticCalculator();
		Ellipsoid reference = Ellipsoid.WGS84;
		GlobalPosition pointA = new GlobalPosition(latA, lonA, 0.0);
		GlobalPosition pointB = new GlobalPosition(latB, lonB, 0.0);
		double result = geoCalc.calculateGeodeticCurve(reference, pointB, pointA).getEllipsoidalDistance();
		return result;
	}
}
