package activities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class Activity2 {
    RequestSpecification requestspec;
    ResponseSpecification responsespec;
    // Set base URL
    @BeforeClass
    public void setup(){
        requestspec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/user")
                .addHeader("Content-Type", "application/json")
                .build();
        responsespec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(3000L))
                .build();
    }

    @Test(priority=1)
    public void addNewUserFromFile() throws IOException {
        FileInputStream inputJSON = new FileInputStream("src/test/java/activities/userinfo.json");
        String reqBody = new String(inputJSON.readAllBytes());
        Response response = given().spec(requestspec)
                .body(reqBody)
                .when().post();
        inputJSON.close();
        response.then().spec(responsespec);
        response.then().body("message", equalTo("9901"));
    }

    @Test(priority=2)
    public void getUserInfo() {
        File outputJSON = new File("src/test/java/activities/userGETResponse.json");
        Response response = given().spec(requestspec)
                .pathParam("username", "justinc")
                .when().get("/{username}");
        String resBody = response.getBody().asPrettyString();
        //System.out.println(resBody);
        try {
            outputJSON.createNewFile();
            FileWriter writer = new FileWriter(outputJSON.getPath());
            writer.write(resBody);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        response.then().body("id", equalTo(9901));
        response.then().body("username", equalTo("justinc"));
        response.then().body("firstName", equalTo("Justin"));
        response.then().body("lastName", equalTo("Case"));
        response.then().body("email", equalTo("justincase@mail.com"));
        response.then().body("password", equalTo("password123"));
        response.then().body("phone", equalTo("9812763450"));
    }

    @Test(priority=3)
    public void deleteUser() throws IOException {
        Response response = given().spec(requestspec)
                .pathParam("username", "justinc")
                .when().delete("/{username}");
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("justinc"));
    }
}