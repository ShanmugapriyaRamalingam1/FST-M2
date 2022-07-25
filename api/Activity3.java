package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class Activity3 {
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
                .expectContentType("application/json")
                .expectBody("status", equalTo("alive"))
                .build();
    }

    @DataProvider
    public Object[][] petInfoProvider() {
        // Setting parameters to pass to test case
        Object[][] testData = new Object[][] {
                { 77232, "Riley", "alive" },
                { 77233, "Hansel", "alive" }
        };
        return testData;
    }

    @Test(priority = 1)
    public void addPet(){
        String reqBody = "{\"id\": 77232, \"name\": \"Riley\", \"status\": \"alive\"}";
        Response response = given().spec(requestspec)
                .body(reqBody)
                .when().post();
        reqBody= "{\"id\": 77233, \"name\": \"Hansel\", \"status\": \"alive\"}";
        response = given().spec(requestspec)
                .body(reqBody)
                .when().post();
        petId = response.then().extract().path("id");
        response.then().spec(responsespec);
    }

    @Test(dataProvider = "petInfoProvider", priority=2)
    public void getPets(int id, String name, String status) {
        Response response = given().spec(requestspec)
                .pathParam("petId", id)
                .when().get("/{petId}");
        System.out.println(response.getBody().asString());
        response.then().spec(responsespec);
        response.then().body("name", equalTo(name));
    }

    @Test(dataProvider = "petInfoProvider", priority=3)
    public void deletePets(int id, String name, String status) {
        Response response = given().spec(requestspec)
                .pathParam("petId", id)
                .when().delete("/{petId}");
        response.then().statusCode(200);
    }
}
