package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ScoreControllerRA {

	private Long existingMovieId, nonExistingMovieId;
	private String adminToken, clientToken, invalidToken;
	private String adminName, adminPassword, clientUsername, clientPassword;

	Map<String, Object> putScoreInstance;

	@BeforeEach
	void setUp() throws Exception {
		baseURI = "http://localhost:8080";
		existingMovieId = 1L;
		nonExistingMovieId = 100L;
		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		adminName = "maria@gmail.com";
		adminPassword = "123456";
		adminToken = TokenUtil.obtainAccessToken(adminName, adminPassword);
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		invalidToken = adminToken + "xyz";
		putScoreInstance = Map.ofEntries(
				Map.entry("movieId", 5L),
				Map.entry("score", 5)
		);
	}

	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		putScoreInstance = Map.ofEntries(
				Map.entry("movieId", 100L),
				Map.entry("score", 5)
		);

		JSONObject newScore = new JSONObject(putScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
					.put("/scores")
				.then()
					.statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		putScoreInstance = Map.ofEntries(
				Map.entry("score", 5)
		);

		JSONObject newScore = new JSONObject(putScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
					.put("/scores")
				.then()
					.statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		putScoreInstance = Map.ofEntries(
				Map.entry("movieId", 100L),
				Map.entry("score", -1)
		);

		JSONObject newScore = new JSONObject(putScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
}
