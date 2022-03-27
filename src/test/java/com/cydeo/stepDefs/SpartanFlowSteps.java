package com.cydeo.stepDefs;

import com.cydeo.pages.pojo.SpartanCreated;
import com.cydeo.utilities.ConfigurationReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.hamcrest.Matchers;
import org.junit.Assert;

import static io.restassured.RestAssured.*;

public class SpartanFlowSteps {

    Response mockSpartanJson;
    Response postResponse;
    Response getResponse;
    SpartanCreated mySpartan;

    @When("User sends a request to Mock API for a mock Spartan Data")
    public void userSendsARequestToMockAPIForAMockSpartanData() {
        baseURI= ConfigurationReader.get("mock.apiUrl");

        mockSpartanJson= given().accept(ContentType.JSON)
                .header("X-API-Key",67878690)
                .when().get();

    //    mockSpartanJson.prettyPrint();

        Assert.assertEquals(200,mockSpartanJson.statusCode());

    }


    @When("User uses Mock Data to create a Spartan")
    public void userUsesMockDataToCreateASpartan() {
        baseURI= ConfigurationReader.get("spartan.apiUrl");

        mySpartan= new SpartanCreated();
        mySpartan.setName(mockSpartanJson.path("name"));
        mySpartan.setGender(mockSpartanJson.path("gender"));
        Long phone= Long.valueOf(mockSpartanJson.path("phone").toString());
        mySpartan.setPhone(phone);
        System.out.println("mySpartan = " + mySpartan);

        postResponse= given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(mySpartan)
                .when().post("api/spartans");

        Assert.assertEquals(201,postResponse.statusCode());
        //        postResponse.prettyPrint();
              /*
        {
    "success": "A Spartan is Born!",
    "data": {
        "id": 174,
        "name": "Heddi",
        "gender": "Female",
        "phone": 2282413422
    }
}
         */



    }

    @When("User send a request to Spartan API with ID {int}")
    public void user_send_a_request_to_spartan_api_with_id(int id) {
        baseURI= ConfigurationReader.get("spartan.apiUrl");

        if(id==0){
            id= postResponse.path("data.id");
        }

        getResponse = given().accept(ContentType.JSON)
                    .and().pathParam("id", id)
                    .when().get("/api/spartans/{id}");

    //    getResponse.prettyPrint();
        Assert.assertEquals(200,getResponse.statusCode());
        /*
        {
    "id": 174,
    "name": "Heddi",
    "gender": "Female",
    "phone": 2282413422
}
         */

    }

    @Then("created Spartan has same info with post request")
    public void createdSpartanHasSameInfoWithPostRequest() {
        String expectedName= postResponse.path("data.name");
        String actualName= getResponse.path("name");
        Assert.assertEquals(expectedName,actualName);

    }

    @And("User update all the fields of created Spartan")
    public void userUpdateAllTheFieldsOfCreatedSpartan() {
        baseURI= ConfigurationReader.get("spartan.apiUrl"); // do not forget

        mySpartan.setName("Jessica");
        mySpartan.setGender("Female");
        mySpartan.setPhone(55545685215l);

        Response putResponse= given()
                .contentType(ContentType.JSON)
                .and()
                .pathParam("id", postResponse.path("data.id"))
                .body(mySpartan)
                .when().put("/api/spartans/{id}");

        Assert.assertEquals(204, putResponse.statusCode());
        Assert.assertEquals("", putResponse.body().asString());

    }

    @Then("User deletes spartan {int}")
    public void userDeletesSpartan(int id) {
        baseURI= ConfigurationReader.get("spartan.apiUrl"); // do not forget
        if(id==0){
            id=postResponse.path("data.id");
        }

        Response deleteResponse= given().pathParam("id",id)
                .when().delete("/api/spartans/{id}");

        Assert.assertEquals(204,deleteResponse.statusCode());

        given().accept(ContentType.JSON)
                .pathParam("id",id)
                .when().get("/api/spartans/{id}")
                .then().statusCode(404)
                .body("error", Matchers.equalToIgnoringCase("Not Found"));
    }


}
