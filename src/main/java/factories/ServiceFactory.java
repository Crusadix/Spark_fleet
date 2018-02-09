package factories;

import services.*;
import services.implementations.*;

public class ServiceFactory {
	
	public BusService getBusService() {
		return new BusServiceImpl();
	}
	
	public BusStopService getStopService() {
		return new BusStopServiceImpl();
	}
	
	public PassengerService getPassengerService() {
		return new PassengerServiceImpl();
	}
}
