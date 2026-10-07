package performance;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigFileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static io.restassured.RestAssured.given;

public class ControlledLoadTest {

    private static final int DURATION_SECONDS = 10;
    private static final int COOLDOWN_SECONDS = 65;

    @Test
    public void runControlledLoadTest() throws Exception {

        ConfigFileReader config = new ConfigFileReader();

        String baseUrl = config.getBaseUrl();
        String loginUrl = baseUrl + "api/login";
        String metricsUrl = baseUrl + "api/metrics";

        String username = config.getUsername();
        String password = config.getPassword();

        // Controlled load profile
        int[] targetRps = {10, 25, 40, 50};

        System.out.println();
        System.out.println("=================================");
        System.out.println("CONTROLLED LOAD TEST");
        System.out.println("=================================");
        System.out.println("Duration per stage: "
                + DURATION_SECONDS + " seconds");
        System.out.println("Cooldown between stages: "
                + COOLDOWN_SECONDS + " seconds");

        for (int stage = 0; stage < targetRps.length; stage++) {

            int rps = targetRps[stage];

            System.out.println();
            System.out.println("---------------------------------");
            System.out.println("Starting Load Stage: " + rps + " RPS");
            System.out.println("Duration: " + DURATION_SECONDS + " seconds");
            System.out.println("---------------------------------");

            // =================================================
            // Metrics BEFORE load
            // =================================================

            Response beforeMetrics =
                    given()
                            .header("Accept", "*/*")
                            .when()
                            .get(metricsUrl);

            Assert.assertEquals(
                    beforeMetrics.getStatusCode(),
                    200,
                    "Metrics API should return HTTP 200 before load"
            );

            int beforeRequests =
                    beforeMetrics.jsonPath().getInt("requests");

            int before5xx =
                    beforeMetrics.jsonPath().getInt("errors_5xx");

            System.out.println("Metrics Before Load:");
            System.out.println("Requests: " + beforeRequests);
            System.out.println("5xx Errors: " + before5xx);

            // =================================================
            // Prepare load
            // =================================================

            int totalRequests =
                    rps * DURATION_SECONDS;

            List<Future<RequestResult>> futures =
                    new ArrayList<>();

            long startTime =
                    System.currentTimeMillis();

            /*
             * Example:
             * 10 RPS = one request every 100 ms
             * 25 RPS = one request every 40 ms
             * 40 RPS = one request every 25 ms
             * 50 RPS = one request every 20 ms
             */
            long intervalMillis =
                    1000L / rps;

            // =================================================
            // Generate load
            // =================================================

            try (ExecutorService executor =
                         Executors.newFixedThreadPool(
                                 Math.min(rps, 20)
                         )) {

                for (int i = 0; i < totalRequests; i++) {

                    long scheduledTime =
                            startTime + (i * intervalMillis);

                    /*
                     * Wait until the scheduled time for
                     * the next request.
                     */
                    while (System.currentTimeMillis()
                            < scheduledTime) {

                        Thread.yield();
                    }

                    Future<RequestResult> future =
                            executor.submit(() ->
                                    sendLoginRequest(
                                            loginUrl,
                                            username,
                                            password
                                    )
                            );

                    futures.add(future);
                }

                /*
                 * IMPORTANT:
                 * Shut down submission first.
                 * Then wait for already-submitted requests
                 * to complete.
                 */
                executor.shutdown();

                boolean completed =
                        executor.awaitTermination(
                                DURATION_SECONDS + 30,
                                TimeUnit.SECONDS
                        );

                if (!completed) {

                    System.out.println(
                            "Warning: Some load requests did not complete within the timeout."
                    );

                    executor.shutdownNow();
                }
            }

            // =================================================
            // Collect results
            // =================================================

            List<RequestResult> results =
                    new ArrayList<>();

            for (Future<RequestResult> future : futures) {

                try {

                    results.add(future.get());

                } catch (CancellationException e) {

                    RequestResult result =
                            new RequestResult();

                    result.statusCode = 599;
                    result.responseTime = 0;
                    result.failureMessage =
                            "Request cancelled";

                    results.add(result);

                } catch (ExecutionException e) {

                    RequestResult result =
                            new RequestResult();

                    result.statusCode = 599;
                    result.responseTime = 0;
                    result.failureMessage =
                            "Client/test execution error";

                    results.add(result);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    RequestResult result =
                            new RequestResult();

                    result.statusCode = 599;
                    result.responseTime = 0;
                    result.failureMessage =
                            "Request interrupted";

                    results.add(result);
                }
            }

            long endTime =
                    System.currentTimeMillis();

            long actualDurationMillis =
                    endTime - startTime;

            // =================================================
            // Calculate results
            // =================================================

            int successCount = 0;
            int failedCount = 0;
            int clientErrorCount = 0;

            int status200 = 0;
            int status401 = 0;
            int status429 = 0;
            int status500 = 0;
            int other4xx = 0;
            int other5xx = 0;

            long totalResponseTime = 0;
            long minResponseTime = Long.MAX_VALUE;
            long maxResponseTime = 0;

            String rateLimitMessage = "";
            String retryAfter = "";

            String serverErrorMessage = "";

            for (RequestResult result : results) {

                int status =
                        result.statusCode;

                // -------------------------------------------------
                // Success / failure
                // -------------------------------------------------

                if (status >= 200 &&
                        status < 300) {

                    successCount++;

                } else {

                    failedCount++;
                }

                // -------------------------------------------------
                // Status code breakdown
                // -------------------------------------------------

                if (status == 200) {

                    status200++;

                } else if (status == 401) {

                    status401++;

                } else if (status == 429) {

                    status429++;

                    if (rateLimitMessage.isEmpty()) {

                        rateLimitMessage =
                                result.failureMessage;
                    }

                    if (retryAfter.isEmpty()) {

                        retryAfter =
                                result.retryAfter;
                    }

                } else if (status == 500) {

                    status500++;

                    if (serverErrorMessage.isEmpty()) {

                        serverErrorMessage =
                                result.failureMessage;
                    }

                } else if (status >= 400 &&
                        status < 500) {

                    other4xx++;

                } else if (status >= 500 &&
                        status < 600) {

                    other5xx++;
                }

                // -------------------------------------------------
                // Client/test execution error
                // -------------------------------------------------

                if (status == 599) {

                    clientErrorCount++;
                }

                // -------------------------------------------------
                // Response time
                // -------------------------------------------------

                totalResponseTime +=
                        result.responseTime;

                if (result.responseTime <
                        minResponseTime) {

                    minResponseTime =
                            result.responseTime;
                }

                if (result.responseTime >
                        maxResponseTime) {

                    maxResponseTime =
                            result.responseTime;
                }
            }

            // =================================================
            // Calculate actual RPS
            // =================================================

            double actualRps =
                    totalRequests /
                            (actualDurationMillis / 1000.0);

            double averageResponseTime =
                    (double) totalResponseTime /
                            totalRequests;

            // =================================================
            // Metrics AFTER load
            // =================================================

            /*
             * Small wait so the application metrics reflect
             * the completed requests.
             */
            Thread.sleep(2000);

            Response afterMetrics =
                    given()
                            .header("Accept", "*/*")
                            .when()
                            .get(metricsUrl);

            Assert.assertEquals(
                    afterMetrics.getStatusCode(),
                    200,
                    "Metrics API should return HTTP 200 after load"
            );

            int afterRequests =
                    afterMetrics.jsonPath().getInt("requests");

            int after5xx =
                    afterMetrics.jsonPath().getInt("errors_5xx");

            int applicationRequestDelta =
                    afterRequests -
                            beforeRequests;

            int application5xxDelta =
                    after5xx -
                            before5xx;

            // =================================================
            // Print results
            // =================================================

            System.out.println();
            System.out.println("=================================");
            System.out.println(
                    "LOAD TEST RESULTS - " +
                            rps + " RPS"
            );
            System.out.println("=================================");

            System.out.println(
                    "Total Requests: " +
                            totalRequests
            );

            System.out.println(
                    "Successful: " +
                            successCount
            );

            System.out.println(
                    "Failed: " +
                            failedCount
            );

            System.out.println(
                    "Client/Connection Errors: " +
                            clientErrorCount
            );

            System.out.println();
            System.out.println("Status Code Breakdown:");

            System.out.println(
                    "200: " +
                            status200
            );

            System.out.println(
                    "401: " +
                            status401
            );

            System.out.println(
                    "429: " +
                            status429
            );

            System.out.println(
                    "500: " +
                            status500
            );

            System.out.println(
                    "Other 4xx: " +
                            other4xx
            );

            System.out.println(
                    "Other 5xx: " +
                            other5xx
            );

            System.out.println();
            System.out.println(
                    "Actual RPS: " +
                            String.format(
                                    "%.2f",
                                    actualRps
                            )
            );

            System.out.println(
                    "Average Response Time: " +
                            String.format(
                                    "%.2f",
                                    averageResponseTime
                            ) +
                            " ms"
            );

            System.out.println(
                    "Minimum Response Time: " +
                            minResponseTime +
                            " ms"
            );

            System.out.println(
                    "Maximum Response Time: " +
                            maxResponseTime +
                            " ms"
            );

            // =================================================
            // Application metrics
            // =================================================

            System.out.println();
            System.out.println("Application Metrics:");

            System.out.println(
                    "Request Delta: " +
                            applicationRequestDelta
            );

            System.out.println(
                    "5xx Error Delta: " +
                            application5xxDelta
            );

            System.out.println(
                    "P50: " +
                            afterMetrics
                                    .jsonPath()
                                    .getString("p50_ms")
            );

            System.out.println(
                    "P95: " +
                            afterMetrics
                                    .jsonPath()
                                    .getString("p95_ms")
            );

            System.out.println(
                    "Orders In Flight: " +
                            afterMetrics
                                    .jsonPath()
                                    .getInt(
                                            "orders_in_flight"
                                    )
            );

            System.out.println(
                    "Version: " +
                            afterMetrics
                                    .jsonPath()
                                    .getString(
                                            "version"
                                    )
            );

            // =================================================
            // Rate limit details
            // =================================================

            if (status429 > 0) {

                System.out.println();
                System.out.println(
                        "Rate Limit Details:"
                );

                System.out.println(
                        "429 Count: " +
                                status429
                );

                System.out.println(
                        "Message: " +
                                rateLimitMessage
                );

                System.out.println(
                        "Retry-After: " +
                                retryAfter
                );
            }

            // =================================================
            // Server error details
            // =================================================

            if (status500 > 0) {

                System.out.println();
                System.out.println(
                        "Server Error Details:"
                );

                System.out.println(
                        "500 Count: " +
                                status500
                );

                System.out.println(
                        "Message: " +
                                serverErrorMessage
                );

                System.out.println(
                        "Metrics 5xx Delta: " +
                                application5xxDelta
                );
            }

            // =================================================
            // Cooldown
            // =================================================

            if (stage < targetRps.length - 1) {

                System.out.println();
                System.out.println(
                        "Cooldown: " +
                                COOLDOWN_SECONDS +
                                " seconds"
                );

                Thread.sleep(
                        COOLDOWN_SECONDS * 1000L
                );
            }
        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("CONTROLLED LOAD TEST COMPLETED");
        System.out.println("=================================");
    }

    // =========================================================
    // Send Login Request
    // =========================================================

    private RequestResult sendLoginRequest(
            String loginUrl,
            String username,
            String password) {

        RequestResult result =
                new RequestResult();

        long startTime =
                System.currentTimeMillis();

        try {

            Response response =
                    given()
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .body(
                                    "{"
                                            + "\"username\":\""
                                            + username
                                            + "\","
                                            + "\"password\":\""
                                            + password
                                            + "\""
                                            + "}"
                            )
                            .when()
                            .post(loginUrl);

            long endTime =
                    System.currentTimeMillis();

            result.statusCode =
                    response.getStatusCode();

            result.responseTime =
                    endTime - startTime;

            // Capture useful information only for failures
            if (response.getStatusCode() >= 400) {

                result.failureMessage =
                        extractSafeMessage(
                                response
                        );

                result.retryAfter =
                        response.getHeader(
                                "Retry-After"
                        );
            }

        } catch (Exception e) {

            long endTime =
                    System.currentTimeMillis();

            result.statusCode = 599;

            result.responseTime =
                    endTime - startTime;

            result.failureMessage =
                    "Client/connection error";

            result.retryAfter = "";
        }

        return result;
    }

    // =========================================================
    // Extract Safe Error Message
    // =========================================================

    private String extractSafeMessage(
            Response response) {

        try {

            String message =
                    response.jsonPath()
                            .getString("detail");

            if (message == null) {

                message =
                        response.jsonPath()
                                .getString("message");
            }

            if (message == null) {

                message =
                        response.jsonPath()
                                .getString("error");
            }

            if (message != null) {

                return message;
            }

        } catch (Exception ignored) {
            // Response may not contain JSON
        }

        return "No readable error message";
    }

    // =========================================================
    // Request Result
    // =========================================================

    private static class RequestResult {

        int statusCode;

        long responseTime;

        String failureMessage = "";

        String retryAfter = "";
    }
}