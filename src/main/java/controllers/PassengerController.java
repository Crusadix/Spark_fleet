package controllers;

import static spark.Spark.*;
import services.*;
import utilities.*;

public class PassengerController {

	public PassengerController(final PassengerService passengerService) {

		get("/passengers", (req, res) -> passengerService.getAllPassengers(), JsonUtils.json());

		post("/passengers",
				(req, res) -> passengerService.moveToBusStop(Integer.valueOf(req.queryParams("passengerId")),
						Integer.valueOf(req.queryParams("stopId"))),
				JsonUtils.json());

		put("/passengers", (req, res) -> {
			return passengerService.createPassenger(req.queryParams("origin"), req.queryParams("destination"));
		}, JsonUtils.json());

		after((req, res) -> {
			res.type("application/json");
		});
	}
}
