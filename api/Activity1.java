package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class Activity1 {
    RequestSpecification requestspec;
    ResponseSpecification responsespec;
    int petId;
    @BeforeClass
    public void setup(){
        requestspec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .addHeader("Content-Type", "application/json")
                //.addHeader("Authorization","token")
                .build();
        responsespec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(3000L))
                .expectBody("status", equalTo("alive"))
                .expectBody("name", equalTo("Ben"))
                .build();
    }

    @Test(priority = 1)
    public void addPet(){
        String reqBody = "{\"id\": 77432, \"name\": \"Riley\", \"status\": \"alive\"}";
        Response response = given().spec(requestspec)
                .body(reqBody)
                .when().post();
        System.out.println(response.getBody().asString());
        petId = response.then().extract().path("id");
        response.then().spec(responsespec);
        response.then().body("message", equalTo(""+petId));
    }

    @Test(priority = 2)
    public void getPet(){
        Response response = given().spec(requestspec)
                .pathParam("petId", petId)
                .when().get("/{petId}");
        System.out.println(response.getBody().asString());
        response.then().spec(responsespec);
        response.then().body("name", equalTo("Riley"));
        response.then().body("status", equalTo("alive"));
    }

    @Test(priority = 3)
    public void deletePet(){
        Response response = given().spec(requestspec)
                .pathParam("petId", petId)
                .when().delete("/{petId}");
        System.out.println(response.getBody().asString());
        response.then().statusCode(200);
        response.then().body("message", equalTo(""+petId));
    }
}
