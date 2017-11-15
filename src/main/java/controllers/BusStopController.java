package controllers;

import static spark.Spark.*;

import services.BusStopService;
import utilities.JsonUtils;

public class BusStopController {

	public BusStopController(final BusStopService busService) {

		get("/stops", (req, res) -> busService.getAllStops(), JsonUtils.json());

		get("/stops/:id", (req, res) -> busService.getStop(Integer.valueOf(req.params("id"))), JsonUtils.json());

		put("/stops", (req, res) -> {

			if (!req.queryParams("fuelType").isEmpty() || !req.queryParams("storage").isEmpty()) {
				return busService.createStation(
						Integer.valueOf(req.queryParams("id")), 
						req.queryParams("fuelType"),
						req.queryParams("storageType"), 
						req.queryParams("location"));
			} else {
				return busService.createBusStop((
						Integer.valueOf(req.queryParams("id"))),
						req.queryParams("location"));
			}

		}, JsonUtils.json());

		after((req, res) -> {
			res.type("application/json");
		});

	}

}
