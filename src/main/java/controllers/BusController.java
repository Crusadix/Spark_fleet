package controllers;

import services.*;
import utilities.*;
import static spark.Spark.*;

public class BusController {

	public BusController(final BusService busService) {

		get("/buses", (req, res) -> busService.getAllBuses(), JsonUtils.json());

		get("/buses/:id", (req, res) -> busService.getBus(Integer.valueOf(req.params("id"))), JsonUtils.json());

		post("/buses/:id", (req, res) -> busService.driveCurrentRoute(Integer.valueOf(req.params("id"))), JsonUtils.json());
		
		put("/buses", (req, res) -> busService.createBus(), JsonUtils.json());

		put("/buses/:id", (req, res) -> busService.setRouteWaypoints(Integer.valueOf(req.params("id")),
				req.queryParams("origin"),
				req.queryParams("destination"),
				req.queryParams("zone")), JsonUtils.json());

		after((req, res) -> {
			res.type("application/json");
		});
	}
}
