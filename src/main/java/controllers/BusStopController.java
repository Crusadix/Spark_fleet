package controllers;

import static spark.Spark.*;

import services.BusStopService;
import utilities.JsonUtils;

public class BusStopController {
	
	public BusStopController(final BusStopService busService) {
		
		get("/stops", (req, res) -> busService.getAllStops(), JsonUtils.json());

		get("/stops/:id", (req, res) -> busService.getStop(Integer.valueOf(req.params("id"))), JsonUtils.json());

		put("/stops", (req, res) -> {
			
			String storageType = req.queryParams("storageType");
			String fuelType = req.queryParams("fuelType");

			if ((storageType != null) || (fuelType != null)) {
				return busService.createStation(
						Integer.valueOf(req.queryParams("id")),
						fuelType,
						storageType,
						req.queryParams("location"));
			} else {
				return busService.createBusStop(
						Integer.valueOf(req.queryParams("id")),
						req.queryParams("location")); 

			} 
		}, JsonUtils.json());

		after((req, res) -> {
			res.type("application/json");
		});

	}

}
