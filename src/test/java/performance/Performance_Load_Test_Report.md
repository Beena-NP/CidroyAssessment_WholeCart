# Performance & Load Test Report

## Objective

A controlled load test was performed against the WholeCart login API to assess application behaviour under increasing request loads up to the documented maximum of 50 Requests Per Second (RPS).

The test was implemented using Java, Rest Assured and TestNG.

## Test Profile

| Parameter | Details |
|---|---|
| API Tested | POST /api/login |
| Load Levels | 10, 25, 40 and 50 RPS |
| Duration | 10 seconds per load level |
| Maximum Target | 50 RPS |
| Monitoring API | GET /api/metrics |
| Metrics Window | 60 seconds |

## Test Results

| Load Level | Total Requests | Successful | Failed | Key Observation |
|---|---:|---:|---:|---|
| 10 RPS | 100 | 100 | 0 | All requests successful |
| 25 RPS | 250 | 250 | 0 | All requests successful |
| 40 RPS | 400 | 399 | 1 | One client-side timeout/connection anomaly |
| 50 RPS | 500 | 472 | 28 | Rate limiting and one server-side error observed |

### 10 RPS

- 100/100 requests returned HTTP 200.
- No 4xx or 5xx errors were observed.
- Actual generated load was approximately 9.90 RPS.
- Average response time: approximately 148 ms.

### 25 RPS

- 250/250 requests returned HTTP 200.
- No 4xx or 5xx errors were observed.
- Actual generated load was approximately 24.74 RPS.
- Average response time: approximately 114 ms.

### 40 RPS

- 399/400 requests completed successfully.
- One client-side timeout/connection anomaly was observed.
- The affected request took approximately 21 seconds to complete.
- Application metrics showed no corresponding 5xx error.
- Therefore, this observation was treated as a client-side/load-generator anomaly rather than a confirmed application 5xx failure.

### 50 RPS

500 requests were generated.

Observed HTTP responses:

| HTTP Status | Count |
|---|---:|
| 200 | 472 |
| 401 | 1 |
| 429 | 26 |
| 500 | 1 |

The HTTP 429 responses returned the message:

`Rate limit: 50 requests per second`

and included:

`Retry-After: 1`

This confirms that rate limiting is enforced at the documented 50 RPS threshold.

One HTTP 500 response was also observed. The `/api/metrics` endpoint confirmed one 5xx error during the corresponding test window.

## Performance Assessment

The application demonstrated stable behaviour at 10 and 25 RPS, with 100% successful HTTP responses during the observed test runs.

At approximately 40 RPS, almost all requests were successful. One unusually long client-side request was observed, but application metrics did not report a corresponding server-side 5xx error.

At 50 RPS, the application reached its documented rate-limit boundary. The HTTP 429 responses are therefore considered expected rate-control behaviour rather than direct evidence that the application cannot process 50 RPS.

However, the occurrence of one HTTP 500 response at the maximum tested load is a performance/load observation that should be investigated further.

## Overall Conclusion

Based on the controlled load testing performed, the WholeCart application showed good stability at low-to-moderate load levels of 10–25 RPS and was able to sustain traffic approaching 40 RPS with only one observed client-side anomaly.

The 50 RPS level should be considered the observed rate-limit boundary rather than a confirmed sustainable application throughput level.

Further testing with longer test durations, repeated executions, additional API workflows and controlled test data would be required to establish a reliable production capacity limit and investigate the observed HTTP 500 error.

### Final Status

**PASS WITH OBSERVATIONS**

The test results provide sufficient evidence for this assessment, but should not be interpreted as a full production-grade performance certification.