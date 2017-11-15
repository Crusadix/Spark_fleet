package controllers;

import services.BusService;
import utilities.JsonUtils;
import static spark.Spark.*;

import interfaces.VehicleInterface;

public class BusController{
	
	public BusController (final BusService busService) {
		
	
		
		get("/buses", (req, res) -> busService.getAllBuses(), 
				JsonUtils.json());

		get("/buses/:id", (req, res) -> busService.getBus(Integer.valueOf(req.params("id"))),
				JsonUtils.json());
	
		/*  
		 * This block can be used to access parameters not visible in the url (bound to request) - needs different endpoint "/buses" is taken
		 * 
		get("/buses", (req, res) -> {return busService.getBus(Integer.valueOf(req.queryParams("id")));
		}, JsonUtils.json());  */
		
		put("/buses", (req, res) -> busService.createBus(Integer.valueOf(req.queryParams("id"))),
				JsonUtils.json());
		
		after((req, res) -> {res.type("application/json");
		});
		
}
	
}
