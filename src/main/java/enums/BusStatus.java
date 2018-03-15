package enums;

public enum BusStatus {
	powerOff (false, "power off"),
	idle (true, "waiting for instructions"),
	stopped (true, "vehicle stopped due to unforseen circumstances"),
	driving (true, "vehicle driving on route"),
	openingDoors (true, "doors opening"),
	closingDoors (true, "doors closing");
	
	private boolean powerOn;
    private String description;
    
    BusStatus(boolean powerOn, String description) {
        this.powerOn = powerOn;
        this.description = description;
    }
    
    boolean powerOn() {
    	return powerOn;
    }
    String status() {
    	return description;
    }
}
