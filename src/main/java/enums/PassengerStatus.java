package enums;

public enum PassengerStatus {
	waiting ("Passenger waiting for a vehicle"),
	idle ("Passenger idling"),
	onBoard ("Passenger on board a vehicle"),
	busOnRoute ("Bus on route to the passenger"),
	delivered ("Passenger delivered to the destination");
	
    private String description;
    
    PassengerStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
    	return description;
    }
}
