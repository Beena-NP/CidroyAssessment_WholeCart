package performance;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigFileReader;

import static io.restassured.RestAssured.given;

public class MetricsPerformanceTest {

    @Test
    public void testMetricsEndpoint() {

        ConfigFileReader config = new ConfigFileReader();

        String metricsUrl = config.getBaseUrl() + "api/metrics";

        long startTime = System.currentTimeMillis();

        Response response =
                given()
                        .header("Accept", "*/*")
                        .when()
                        .get(metricsUrl);

        long endTime = System.currentTimeMillis();

        long responseTime = endTime - startTime;

        System.out.println("=================================");
        System.out.println("Metrics API URL: " + metricsUrl);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Time: " + responseTime + " ms");
        System.out.println("Requests: " +
                response.jsonPath().getInt("requests"));
        System.out.println("5xx Errors: " +
                response.jsonPath().getInt("errors_5xx"));
        System.out.println("P50: " +
                response.jsonPath().getString("p50_ms"));
        System.out.println("P95: " +
                response.jsonPath().getString("p95_ms"));
        System.out.println("Orders In Flight: " +
                response.jsonPath().getInt("orders_in_flight"));
        System.out.println("Version: " +
                response.jsonPath().getString("version"));

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Metrics API should return HTTP 200"
        );

        Assert.assertEquals(
                response.jsonPath().getInt("window_seconds"),
                60,
                "Metrics window should be 60 seconds"
        );

        Assert.assertNotNull(
                response.jsonPath().get("requests"),
                "Requests metric should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("errors_5xx"),
                "5xx error metric should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("orders_in_flight"),
                "Orders in flight metric should be present"
        );

        Assert.assertNotNull(
                response.jsonPath().get("version"),
                "Version should be present"
        );
    }
}