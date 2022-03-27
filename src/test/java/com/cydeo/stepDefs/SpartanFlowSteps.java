package com.cydeo.stepDefs;

import com.cydeo.pages.pojo.SpartanCreated;
import com.cydeo.utilities.ConfigurationReader;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class SpartanFlowSteps {

    Response mockSpartanJson;
    Response postResponse;

    @When("User sends a request to Mock API for a mock Spartan Data")
    public void userSendsARequestToMockAPIForAMockSpartanData() {
        baseURI= ConfigurationReader.get("mock.apiUrl");

        mockSpartanJson= given().accept(ContentType.JSON)
                .header("X-API-Key",67878690)
                .when().get();

        mockSpartanJson.prettyPrint();

    }


    @When("User uses Mock Data to create a Spartan")
    public void userUsesMockDataToCreateASpartan() {
        baseURI= ConfigurationReader.get("spartan.apiUrl");

        SpartanCreated mySpartan= new SpartanCreated();
        mySpartan.setName(mockSpartanJson.path("name"));
        mySpartan.setGender(mockSpartanJson.path("gender"));
        Long phone= Long.valueOf(mockSpartanJson.path("phone").toString());
        mySpartan.setPhone(phone);
        System.out.println("mySpartan = " + mySpartan);

        postResponse= given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(mySpartan)
                .when().post("api/spartans");

        postResponse.prettyPrint();

    }

    @When("User send a request to Spartan API with ID {int}")
    public void user_send_a_request_to_spartan_api_with_id(int id) {
        baseURI= ConfigurationReader.get("spartan.apiUrl");

        if(id==0){
            id= postResponse.path("data.id");
        }

        Response response = given().accept(ContentType.JSON)
                    .and().pathParam("id", id)
                    .when().get("/api/spartans/{id}");

        response.prettyPrint();

    }


}
