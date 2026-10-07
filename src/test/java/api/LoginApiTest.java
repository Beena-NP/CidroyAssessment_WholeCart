package api;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ConfigFileReader;

import static io.restassured.RestAssured.given;

public class LoginApiTest {

    @DataProvider(name = "loginAccounts")
    public Object[][] loginAccounts() {

        return new Object[][] {
                {"buyer1", "buyer"},
                {"buyer2", "buyer"},
                {"buyer3", "buyer"},
                {"seller1", "seller"},
                {"seller2", "seller"},
                {"seller3", "seller"},
                {"operator", "operator"}
        };
    }

    @Test(dataProvider = "loginAccounts")
    public void testLoginApi(String username, String expectedRole) {

        ConfigFileReader config = new ConfigFileReader();

        String password = config.getPassword();
        String apiUrl = config.getBaseUrl() + "api/login";

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body("{"
                                + "\"username\":\"" + username + "\","
                                + "\"password\":\"" + password + "\""
                                + "}")
                        .when()
                        .post(apiUrl);

        System.out.println("=================================");
        System.out.println("Username: " + username);
        System.out.println("Status Code: " + response.getStatusCode());
       // System.out.println("Response: " + response.asPrettyString());
        System.out.println("Role: " + response.jsonPath().getString("role"));
        System.out.println("Name: " + response.jsonPath().getString("name"));

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Login API should return HTTP 200"
        );

        Assert.assertNotNull(
                response.jsonPath().getString("token"),
                "Token should be present"
        );

        Assert.assertEquals(
                response.jsonPath().getString("role"),
                expectedRole,
                "Role returned by API is incorrect"
        );

        Assert.assertNotNull(
                response.jsonPath().getString("name"),
                "Name should be present"
        );
    }
}