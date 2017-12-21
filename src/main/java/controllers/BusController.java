package controllers;

import services.*;
import utilities.*;
import static spark.Spark.*;

public class BusController {

	public BusController(final BusService busService) {

		get("/buses", (req, res) -> busService.getAllBuses(), JsonUtils.json());

		get("/buses/:id", (req, res) -> busService.getBus(Integer.valueOf(req.params("id"))), JsonUtils.json());

		post("/buses/:id", (req, res) -> busService.driveCurrentRoute(Integer.valueOf(req.params("id")),
				req.queryParams("operationType")), JsonUtils.json());
		
		put("/buses", (req, res) -> busService.createBus(), JsonUtils.json());

		put("/buses/:id", (req, res) -> {
			String waypoints = req.queryParams("waypoints");
			if (waypoints.equals("onDemand")) {
				return busService.setRouteWaypointsOnDemand(Integer.valueOf(req.params("id")),
						req.queryParams("origin"),
						req.queryParams("destination"),
						req.queryParams("zone"));
			} else {
				return busService.setRouteWaypoints(Integer.valueOf(req.params("id")),
						req.queryParams("origin"),
						req.queryParams("destination"),
						waypoints,
						req.queryParams("zone"));
				
			}
		}, JsonUtils.json());
		
		options("/*",
		        (request, response) -> {

		            String accessControlRequestHeaders = request
		                    .headers("Access-Control-Request-Headers");
		            if (accessControlRequestHeaders != null) {
		                response.header("Access-Control-Allow-Headers",
		                        accessControlRequestHeaders);
		            }

		            String accessControlRequestMethod = request
		                    .headers("Access-Control-Request-Method");
		            if (accessControlRequestMethod != null) {
		                response.header("Access-Control-Allow-Methods",
		                        accessControlRequestMethod);
		            }

		            return "OK";
		        });

		before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
	
		after((req, res) -> {
			res.type("application/json");
		});
	}
}
