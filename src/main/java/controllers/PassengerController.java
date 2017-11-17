package controllers;

import static spark.Spark.*;

import services.PassengerService;
import utilities.JsonUtils;

public class PassengerController {

	public PassengerController(final PassengerService passengerServie) {

		get("/passengers", (req, res) -> passengerServie.getAllPassengers(), JsonUtils.json());

		put("/passengers", (req, res) -> {

			return passengerServie.createPassenger(
					Integer.valueOf(req.queryParams("id")), 
					req.queryParams("origin"),
					req.queryParams("destination"));

		}, JsonUtils.json());

		after((req, res) -> {
			res.type("application/json");
		});

	}

}
