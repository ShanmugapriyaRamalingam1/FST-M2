package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GitHubProject {

    RequestSpecification requestspec;
    String sshKey;
    int id;
    @BeforeClass
    public void setUP()
    {
        requestspec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","token samplevalue")
                .build();
    }

    @Test(priority = 1)
    public void addSSHKey(){
        sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCuybidGp/PsclDZztzyPxptPY39Kv7FKQJmScEAtc4PAqvhIzZHpN/xA0GwC0ACNefet2OH01xMd3YzhgP7z1HrO1deDEqJnGAK6bRK+USxhxnCH00EGvwKokC9utx/DfecQNlOT3S7JrQuvlK59D2ArqJQgVAplF3d16W+7jo/mg5I7hwUIT/EQS7ajHjTZLTs5SoirOJYNtkjf8K+zPB0qEHRN5c02F6KBqLkrioIHtLFwIsP5oXusH6J7mizwFl25FlcL+OICncNVaNrrDXBqGp9Qxl3tql1AICHSA4+2AQ4zPFMjTwFO80IoIekPHrEVBD8fVEZdAc0Qd5Vog3";
        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \""+sshKey+"\"}";
        Response response = given().spec(requestspec)
                .body(reqBody)
                .when().post("/user/keys");
       //System.out.println(response.getBody().asString());
        id = response.then().extract().path("id");
        response.then().statusCode(201);
    }

    @Test(priority = 2)
    public void getKey(){
        Response response = given().spec(requestspec)
                .pathParam("keyId", id)
                .when().get("/user/keys/{keyId}");
        //System.out.println(response.getBody().asString());
        response.then().statusCode(200);
        Reporter.log(response.getBody().asString());
    }

    @Test(priority = 3)
    public void deleteKey(){
        Response response = given().spec(requestspec)
                .pathParam("keyId", id)
                .when().delete("/user/keys/{keyId}");
        //System.out.println(response.getBody().asString());
        response.then().statusCode(204);
        Reporter.log(response.getBody().asString());
    }

}
