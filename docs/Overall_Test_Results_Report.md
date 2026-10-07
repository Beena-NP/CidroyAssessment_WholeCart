# WholeCart B2B Marketplace – Overall Test Results Report

## 1. Executive Summary

Testing was performed on the WholeCart B2B Marketplace covering UI authentication, API authentication, application metrics and controlled performance/load testing.

The implemented automation successfully validated login functionality for all documented Buyer, Seller and Operator accounts.

Performance testing was conducted at increasing loads up to the documented maximum target of 50 RPS. The application demonstrated stable behaviour at 10 and 25 RPS. At 50 RPS, rate limiting was observed as expected, along with one HTTP 500 server-side error that requires further investigation.

The assessment identified one confirmed defect and additional performance observations.

## 2. Test Coverage Summary

| Test Area              | Coverage                      | Result                 |
| ---------------------- | ----------------------------- | ---------------------- |
| Buyer UI Login         | 3 accounts                    | 3/3 Passed             |
| Seller UI Login        | 3 accounts                    | 3/3 Passed             |
| Operator UI Login      | 1 account                     | 1/1 Passed             |
| Buyer API Login        | 3 accounts                    | 3/3 Passed             |
| Seller API Login       | 3 accounts                    | 3/3 Passed             |
| Operator API Login     | 1 account                     | 1/1 Passed             |
| Metrics API            | 1 endpoint                    | Passed                 |
| Performance / Load     | 10, 25, 40, 50 RPS            | Pass with observations |
| Business E2E Workflows | Identified in scenario matrix | Pending execution      |

## 3. Automation Results

### UI Automation

The Selenium/TestNG automation validated successful login for all seven documented accounts.

**Result: 7/7 Passed**

Role-specific landing pages were validated:

* Buyer → `Catalogue · WholeCart`
* Seller → `Orders · Seller · WholeCart`
* Operator → `Settings · WholeCart`

### API Automation

The Rest Assured/TestNG automation validated the login API for all seven documented accounts.

For each account, the following were validated:

* HTTP 200 response
* Authentication token presence
* Correct role
* User name presence

**Result: 7/7 Passed**

### Metrics API

The `/api/metrics` endpoint was validated for:

* HTTP 200 response
* 60-second metrics window
* Request count
* 5xx error count
* Orders in flight
* Application version

**Result: Passed**

## 4. Performance & Load Testing

The login API was tested at 10, 25, 40 and 50 RPS.

| Target | Requests |   Successful | Key Result                                 |
| -----: | -------: | -----------: | ------------------------------------------ |
| 10 RPS |      100 |          100 | Stable                                     |
| 25 RPS |      250 |          250 | Stable                                     |
| 40 RPS |      400 |          399 | One client-side timeout/connection anomaly |
| 50 RPS |      500 | 472 HTTP 200 | Rate limiting and one HTTP 500 observed    |

At 50 RPS, 26 requests returned HTTP 429 with the message:

`Rate limit: 50 requests per second`

This is considered expected rate-limiting behaviour.

One HTTP 500 response was observed and confirmed by the `/api/metrics` 5xx counter.

**Performance Status: PASS WITH OBSERVATIONS**

Detailed results are documented in `performance/Performance_Load_Test_Report.md`.

## 5. Defect Summary

| ID      | Description                                       | Severity | Priority | Status |
| ------- | ------------------------------------------------- | -------- | -------- | ------ |
| DEF-001 | HTTP 500 observed during login API load at 50 RPS | High     | High     | Open   |

### DEF-001

One server-side HTTP 500 error occurred during the 50 RPS test.

The application metrics confirmed one 5xx error during the corresponding test window.

Further investigation of server/application logs is required to determine the root cause.

## 6. Additional Observations

### OBS-001 – Client-Side Timeout

One unusually long request of approximately 21 seconds was observed during the 40 RPS test.

The application metrics did not report a corresponding 5xx error. Therefore, this has been recorded as a performance observation rather than a confirmed application defect.

### OBS-002 – Rate Limiting

HTTP 429 responses were observed at the 50 RPS boundary.

The API explicitly returned a rate-limit message and `Retry-After: 1`, indicating intentional rate-control behaviour.

## 7. Business Workflow Coverage

The following major workflows were identified but were not fully executed within the available assessment time:

* Product catalogue validation
* Add/update/remove cart operations
* Checkout
* B2B order creation
* Seller order processing
* Buyer order-status validation
* Tax invoice validation
* CR-09 invoice validation
* Role-based unauthorized-access scenarios
* Duplicate-order prevention

These scenarios are documented in the Scenario Matrix and should be included in a complete regression cycle.

## 8. Test Limitations

The following limitations apply to the assessment results:

1. Testing was performed against the provided assessment environment.
2. No production infrastructure or capacity certification was performed.
3. Performance testing was limited to the login API.
4. Load-test duration was intentionally short due to assessment time constraints.
5. Long-duration endurance/soak testing was not performed.
6. Server/application logs were not available for root-cause analysis of the HTTP 500.
7. Not all end-to-end business workflows could be executed within the available assessment window.

## 9. Overall Assessment

The implemented automation provides good baseline coverage for authentication across all documented user roles.

The application demonstrated stable behaviour under 10 and 25 RPS load levels. Traffic approaching 40 RPS was largely successful, although one client-side performance anomaly was observed.

At 50 RPS, the documented rate-limiting mechanism was triggered as expected. However, one HTTP 500 server-side error was also observed and should be investigated before considering the maximum load level fully stable.

The overall assessment result is:

### **PASS WITH OBSERVATIONS**

The application shows acceptable behaviour for the tested authentication scenarios and lower load levels, but further functional workflow testing and investigation of the 50 RPS HTTP 500 error are recommended before production readiness can be confirmed.

## 10. Recommended Next Steps

1. Investigate the root cause of DEF-001 using application/server logs.
2. Repeat the 50 RPS test after the defect is addressed.
3. Execute the complete Buyer → Seller → Order → Invoice end-to-end workflow.
4. Validate all CR-09 invoice requirements.
5. Complete role-based authorization and negative testing.
6. Perform longer-duration performance testing.
7. Define formal API response-time and throughput acceptance criteria.
