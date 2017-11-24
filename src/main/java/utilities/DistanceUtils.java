package utilities;

import org.gavaghan.geodesy.*;

public class DistanceUtils {

	private static DistanceUtils distanceUtils = new DistanceUtils();

	private DistanceUtils() {
	}

	public static DistanceUtils getInstance() {
		return distanceUtils;
	}

	public double getDistanceMeters(String coordsA, String coordsB) {
		String[] partsA = coordsA.split(",");
		String[] partsB = coordsB.split(",");
		double latA = Double.parseDouble(partsA[0]);
		double lonA = Double.parseDouble(partsA[1]);
		double latB = Double.parseDouble(partsB[0]);
		double lonB = Double.parseDouble(partsB[1]);
		GeodeticCalculator geoCalc = new GeodeticCalculator();
		Ellipsoid reference = Ellipsoid.WGS84;
		GlobalPosition pointA = new GlobalPosition(latA, lonA, 0.0);
		GlobalPosition pointB = new GlobalPosition(latB, lonB, 0.0);
		double result = geoCalc.calculateGeodeticCurve(reference, pointB, pointA).getEllipsoidalDistance();
		return result;
	}
}
