package interfaces;

import com.google.maps.model.LatLng;

import enums.PassengerStatus;

public interface PassengerInterface {

	/**
	 * Returns the id of the passenger. Id is used for a multitude of different operations.
	 * 
	 * @return returns the int-value representing the id of the passenger
	 */
	int getId();

	/**
	 * Returns location of the destination as a LatLng-object (coordinates) of the passenger. 
	 * 
	 * @return returns the LatLng-object representing the location of the destination.
	 */
	LatLng getDestinationCoords();

	/**
	 * Sets the status of the passenger to the specified BusStatus-enum.
	 * 
	 * @param status specifies the status to be set for the passenger
	 */
	void setStatus(PassengerStatus status);

	/**
	 * Sets the currentCoords-variable of the passenger to the specified LatLng-object.
	 * 
	 * @param currentCoords specifies the LatLng-object the passenger should currently be at
	 */
	void setCurrentCoords(LatLng currentCoords);

	/**
	 * Returns current location of the passenger as a LatLng-object (coordinates). 
	 * 
	 * @return returns the LatLng-object representing the current location of the passenger.
	 */
	LatLng getCurrentCoords();

	/**
	 * Returns current status of the passenger as a BusStatus-enum. 
	 * 
	 * @return returns the BusStatus-enum representing the current status of the passenger.
	 */
	PassengerStatus getStatus();

	/**
	 * Returns a LatLng-object representing the origin of the passenger (the point where 
	 * routing was started for the passenger).
	 * 
	 * @return returns the LatLng-object representing the origin of the passenger.
	 */
	LatLng getOriginCoords();
}
