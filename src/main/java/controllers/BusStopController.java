package controllers;

import static spark.Spark.*;

import services.BusStopService;
import utilities.JsonUtils;

public class BusStopController {
	
	public BusStopController(final BusStopService busStopService) {

		get("/stops", (req, res) -> busStopService.getAllStops(), JsonUtils.json());

		get("/stops/:id", (req, res) -> busStopService.getStop(Integer.valueOf(req.params("id"))), JsonUtils.json());

		put("/stops", (req, res) -> {

			String storageType = req.queryParams("storageType");
			String fuelType = req.queryParams("fuelType");

			if ((storageType != null) || (fuelType != null)) {
				return busStopService.createStation(Integer.valueOf(req.queryParams("id")), fuelType, storageType,
						req.queryParams("location"));
			} else {
				return busStopService.createBusStop(Integer.valueOf(req.queryParams("id")), req.queryParams("location"));

			}
		}, JsonUtils.json());

		after((req, res) -> {
			res.type("application/json");
		});

	}

}
