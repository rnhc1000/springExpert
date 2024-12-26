package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MovieControllerRA {
  private Integer existingMovieId, nonExistingMovieId;
  private String getTitle;
  private String addTitle;
  private Map<String, Object> postMovieInstance;
  private Double addScore;
  private Integer addCount;
  private String addImage;
  private String adminToken, clientToken, invalidToken;
  private String adminName, adminPassword, clientUsername, clientPassword;

  @BeforeEach
  void setUp() throws Exception {

    baseURI = "http://localhost:8080";

    existingMovieId = 11;
    getTitle = "O Espetacular Homem-Aranha 2: A Ameaça de Electro";
    addTitle = "TestMovie";
    addScore = 0.0D;
    addCount = 0;
    addImage = "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg";
    postMovieInstance = Map.ofEntries(
        Map.entry("title", addTitle),
        Map.entry("score", addScore),
        Map.entry("count", addCount),
        Map.entry("image", addImage)
    );

    clientUsername = "alex@gmail.com";
    clientPassword = "123456";
    adminName = "maria@gmail.com";
    adminPassword = "123456";
    adminToken = TokenUtil.obtainAccessToken(adminName, adminPassword);
    clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
    invalidToken = adminToken + "xyz";

  }

  @Test
  public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {

    given()
        .get("/movies")
        .then()
        .statusCode(200)
        .body("content[10].id", is(11))
        .body("content[2].title", is("O Espetacular Homem-Aranha 2: A Ameaça de Electro"));
  }

  @Test
  public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {

    given()
        .get("/movies?title={title}", getTitle)
        .then()
        .statusCode(200)
        .body("totalElements", is(1))
        .body("content[0].id", is(3))
        .body("content[0].title", equalTo(getTitle));
  }

  @Test
  public void findByIdShouldReturnMovieWhenIdExists() {

    existingMovieId = 11;
    given()
        .get("/movies/{id}", existingMovieId)
        .then()
        .statusCode(200)
        .body("id", is(existingMovieId))
        .body("title", equalTo("Star Wars: A Guerra dos Clones"))
        .body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/uK15I3sGd8AudO9z6J6vi0HH1UU.jpg"))
        .body("score", is(0.0F));
  }

  @Test
  public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
    nonExistingMovieId = 50;

    given()
        .get("/movies/{id}", nonExistingMovieId)
        .then()
        .statusCode(404);
  }

  @Test
  public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {

    addTitle = "";
    addScore = 0.0D;
    addCount = 0;
    addImage = "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg";
    postMovieInstance = Map.ofEntries(
        Map.entry("title", addTitle),
        Map.entry("score", addScore),
        Map.entry("count", addCount),
        Map.entry("image", addImage)
    );
    JSONObject newMovie = new JSONObject(postMovieInstance);

    given()
        .header("Content-type", "application/json")
        .header("Authorization", "Bearer " + adminToken)
        .body(newMovie)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
          .post("/movies")
          .then()
        .statusCode(422);
  }

  @Test
  public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {

    JSONObject newMovie = new JSONObject(postMovieInstance);

    given()
        .header("Content-type", "application/json")
        .header("Authorization", "Bearer " + clientToken)
        .body(newMovie)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post("/movies")
        .then()
        .statusCode(403);
  }

  @Test
  public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

    JSONObject newMovie = new JSONObject(postMovieInstance);

    given()
        .header("Content-type", "application/json")
        .header("Authorization", "Bearer " + invalidToken)
        .body(newMovie)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post("/movies")
        .then()
        .statusCode(401);
  }
}
