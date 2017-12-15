package interfaces;

public interface PassengerInterface {

	int getId();

	String getDestinationCoords();

	void setStatus(String string);

	void setCurrentCoords(String currentCoords);

	String getCurrentCoords();

	String getStatus();
}
