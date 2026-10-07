# WholeCart B2B Marketplace – Defect Log

## Defect Summary

| Defect ID | Area              | Severity | Priority | Status |
| --------- | ----------------- | -------- | -------- | ------ |
| DEF-001   | API / Performance | High     | High     | Open   |

---

## DEF-001 – HTTP 500 Error Observed at 50 RPS

### Defect Information

**Defect ID:** DEF-001
**Title:** HTTP 500 Internal Server Error observed during login API load at 50 RPS
**Area:** API / Performance / Load
**Severity:** High
**Priority:** High
**Status:** Open
**Environment:** WholeCart Assessment Environment
**Endpoint:** `POST /api/login`

### Description

During controlled load testing of the WholeCart login API at the documented maximum target of **50 requests per second**, one HTTP 500 Internal Server Error was observed.

The test generated 500 login requests.

### Steps to Reproduce

1. Start the WholeCart assessment environment.
2. Configure valid buyer credentials using environment variables.
3. Execute the controlled load test against:
   `POST /api/login`
4. Generate approximately 50 requests per second.
5. Continue the load for approximately 10 seconds.
6. Monitor the `/api/metrics` endpoint after the test.
7. Review the HTTP response status distribution.

### Test Data

* Valid WholeCart login credentials
* Target load: 50 RPS
* Total requests: 500

### Expected Result

All valid login requests should be processed successfully or, where the documented rate limit is exceeded, should return the expected rate-limit response.

The application should not return an unexpected HTTP 500 Internal Server Error for a valid login request.

### Actual Result

The following response distribution was observed:

| HTTP Status | Count |
| ----------- | ----: |
| 200         |   472 |
| 401         |     1 |
| 429         |    26 |
| 500         |     1 |

The HTTP 429 responses explicitly returned:

`Rate limit: 50 requests per second`

with:

`Retry-After: 1`

One HTTP 500 response was observed without a readable application error message.

The `/api/metrics` endpoint also showed an increase of **1 in `errors_5xx`**, confirming that the server recorded one 5xx error during the test window.

### Evidence

**Load Test Result:**

* Total requests: 500
* Successful HTTP 200 responses: 472
* HTTP 429 responses: 26
* HTTP 401 responses: 1
* HTTP 500 responses: 1

**Metrics:**

* 5xx error delta: 1
* Application version: 1.0
* Orders in flight: 0

### Impact

A server-side error occurred while the API was operating at the documented maximum load threshold.

Although the occurrence was limited to one request in the observed test run, the error indicates that additional investigation is required before confirming the API as fully stable at the 50 RPS boundary.

### Initial Assessment

The HTTP 429 responses are **not considered defects**, as they are consistent with the documented rate-limiting behaviour.

The HTTP 500 response is considered a genuine server-side defect because:

1. The response status was HTTP 500.
2. The request was generated using valid login credentials.
3. The `/api/metrics` endpoint recorded one 5xx error.
4. The error was observed during controlled load testing.

### Recommended Investigation

The development team should review:

* Application/server logs corresponding to the failed request.
* Exception stack trace for the HTTP 500 response.
* Rate-limiter behaviour around the 50 RPS boundary.
* Concurrent request handling in the login API.
* Whether the failure is reproducible under repeated 50 RPS tests.
* Whether the 500 is related to resource contention or another transient server condition.

---

# Performance Observations

## OBS-001 – Long Client-Side Request at 40 RPS

During the 40 RPS test:

* Total requests: 400
* Successful requests: 399
* One request experienced an unusually long completion time of approximately 21 seconds.
* The load generator classified this as a client-side timeout/connection anomaly.
* `/api/metrics` showed **0 additional application 5xx errors**.

### Assessment

This has **not been classified as a confirmed application defect** because there was no corresponding server-side 5xx error.

It should be investigated if the behaviour is reproducible in repeated load tests.

---

# Expected Rate-Limiting Behaviour

## OBS-002 – HTTP 429 at 50 RPS

During the 50 RPS test, 26 requests received HTTP 429.

The API returned:

`Rate limit: 50 requests per second`

and:

`Retry-After: 1`

### Assessment

This is considered **expected behaviour**, not a defect, because it is consistent with the documented rate-limit boundary.

The observation has been retained in the performance report for completeness.

---

# Defect Triage Summary

| ID      | Type          | Finding                                      | Classification     |
| ------- | ------------- | -------------------------------------------- | ------------------ |
| DEF-001 | Server Error  | HTTP 500 at 50 RPS                           | Defect – High      |
| OBS-001 | Performance   | One ~21-second client-side request at 40 RPS | Observation        |
| OBS-002 | Rate Limiting | HTTP 429 responses at 50 RPS                 | Expected Behaviour |

## Evidence Availability

The following evidence is available in the test execution output and performance report:

* HTTP response status distribution
* Load test request counts
* Error counts
* `/api/metrics` 5xx count
* Rate-limit response message
* Retry-After header
* Performance/load test summary

No application server logs were available in the assessment environment; therefore, root cause of DEF-001 could not be determined from the test side alone.
