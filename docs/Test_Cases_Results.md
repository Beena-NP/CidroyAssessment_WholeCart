# WholeCart B2B Marketplace – Test Cases & Results

## 1. Execution Summary

Testing was performed across UI, API and basic performance/load areas using the provided WholeCart assessment environment.

| Area                        |    Test Cases |                 Passed | Failed | Not Executed |
| --------------------------- | ------------: | ---------------------: | -----: | -----------: |
| UI Login                    |             7 |                      7 |      0 |            0 |
| API Login                   |             7 |                      7 |      0 |            0 |
| Metrics API                 |             1 |                      1 |      0 |            0 |
| Performance / Load          | 4 load levels | Pass with observations |      - |            0 |
| Detailed Business Workflows |       Planned |                      - |      - |      Pending |

---

# 2. UI Test Cases

## TC-UI-001 – Buyer 1 Valid Login

**Objective:** Verify that Buyer 1 can log in successfully.

**Test Data:**

* Username: `buyer1`
* Password: Valid configured test password

**Steps:**

1. Open the WholeCart application.
2. Enter Buyer 1 username.
3. Enter the valid password.
4. Click Login.

**Expected Result:**

* User should be authenticated successfully.
* User should be redirected to the Buyer Catalogue page.

**Actual Result:**

* User was successfully authenticated.
* Page title displayed: `Catalogue · WholeCart`

**Status:** PASS

---

## TC-UI-002 – Buyer 2 Valid Login

**Test Data:** `buyer2`

**Expected Result:** Buyer should successfully log in and land on the Catalogue page.

**Actual Result:** Page title: `Catalogue · WholeCart`

**Status:** PASS

---

## TC-UI-003 – Buyer 3 Valid Login

**Test Data:** `buyer3`

**Expected Result:** Buyer should successfully log in and land on the Catalogue page.

**Actual Result:** Page title: `Catalogue · WholeCart`

**Status:** PASS

---

## TC-UI-004 – Seller 1 Valid Login

**Test Data:** `seller1`

**Expected Result:** Seller should successfully log in and land on the Seller Orders page.

**Actual Result:** Page title: `Orders · Seller · WholeCart`

**Status:** PASS

---

## TC-UI-005 – Seller 2 Valid Login

**Test Data:** `seller2`

**Expected Result:** Seller should successfully log in and land on the Seller Orders page.

**Actual Result:** Page title: `Orders · Seller · WholeCart`

**Status:** PASS

---

## TC-UI-006 – Seller 3 Valid Login

**Test Data:** `seller3`

**Expected Result:** Seller should successfully log in and land on the Seller Orders page.

**Actual Result:** Page title: `Orders · Seller · WholeCart`

**Status:** PASS

---

## TC-UI-007 – Operator Valid Login

**Test Data:** `operator`

**Expected Result:** Operator should successfully log in and land on the Operator Settings page.

**Actual Result:** Page title: `Settings · WholeCart`

**Status:** PASS

---

# 3. API Test Cases

## TC-API-001 to TC-API-007 – Valid Login API

The Login API was tested using all seven documented test accounts.

**Endpoint:**

`POST /api/login`

| Test Case  | Username | Expected Role | Result                                     | Status |
| ---------- | -------- | ------------- | ------------------------------------------ | ------ |
| TC-API-001 | buyer1   | buyer         | HTTP 200, token and buyer role returned    | PASS   |
| TC-API-002 | buyer2   | buyer         | HTTP 200, token and buyer role returned    | PASS   |
| TC-API-003 | buyer3   | buyer         | HTTP 200, token and buyer role returned    | PASS   |
| TC-API-004 | seller1  | seller        | HTTP 200, token and seller role returned   | PASS   |
| TC-API-005 | seller2  | seller        | HTTP 200, token and seller role returned   | PASS   |
| TC-API-006 | seller3  | seller        | HTTP 200, token and seller role returned   | PASS   |
| TC-API-007 | operator | operator      | HTTP 200, token and operator role returned | PASS   |

### API Validations Performed

For each successful login:

* HTTP status code validated as `200`
* Authentication token validated for presence
* Returned role validated
* User name validated for presence
* Expected role matched the supplied account

All seven API login test cases passed.

---

# 4. Metrics API Test Case

## TC-API-008 – Metrics Endpoint Validation

**Endpoint:**

`GET /api/metrics`

**Expected Result:**

* HTTP 200
* 60-second metrics window
* Request count available
* 5xx error count available
* Orders in flight available
* Version available

**Actual Result:**

* HTTP 200
* `window_seconds`: 60
* `errors_5xx`: 0 during baseline execution
* `orders_in_flight`: 0
* `version`: 1.0

**Status:** PASS

---

# 5. Performance / Load Test Results

The login API was tested at increasing load levels of 10, 25, 40 and 50 RPS.

| Target RPS | Requests | Successful | Failed | Observation                                |
| ---------: | -------: | ---------: | -----: | ------------------------------------------ |
|         10 |      100 |        100 |      0 | Stable                                     |
|         25 |      250 |        250 |      0 | Stable                                     |
|         40 |      400 |        399 |      1 | One client-side timeout/connection anomaly |
|         50 |      500 |        472 |     28 | Rate limiting and one HTTP 500 observed    |

### 50 RPS Detailed Observation

The 50 RPS test produced:

* 472 × HTTP 200
* 1 × HTTP 401
* 26 × HTTP 429
* 1 × HTTP 500

The HTTP 429 responses explicitly indicated:

`Rate limit: 50 requests per second`

and returned:

`Retry-After: 1`

This indicates that the API enforces rate limiting at the documented threshold.

One HTTP 500 response was also observed. The `/api/metrics` endpoint confirmed one 5xx error during the corresponding test window.

**Performance Status:** PASS WITH OBSERVATIONS

Detailed performance findings are documented in:

`performance/Performance_Load_Test_Report.md`

---

# 6. Business Workflow Test Coverage

The following scenarios were identified as required coverage but were not fully executed within the available assessment time.

| Scenario                         | Status                 |
| -------------------------------- | ---------------------- |
| Buyer product catalogue          | Planned / Not Executed |
| Add product to cart              | Planned / Not Executed |
| Update cart quantity             | Planned / Not Executed |
| Checkout                         | Planned / Not Executed |
| B2B order creation               | Planned / Not Executed |
| Seller views buyer order         | Planned / Not Executed |
| Seller updates order status      | Planned / Not Executed |
| Buyer views updated order status | Planned / Not Executed |
| Tax invoice validation           | Planned / Not Executed |
| CR-09 invoice validation         | Planned / Not Executed |
| Role-based unauthorized access   | Planned / Not Executed |
| Duplicate order prevention       | Planned / Not Executed |

These scenarios remain part of the identified test coverage and should be executed in a complete regression cycle when sufficient time and test data are available.

---

# 7. Test Evidence

Evidence for executed automation tests includes:

* TestNG execution results
* Console output from UI login tests
* Console output from API login tests
* HTTP status and response validations
* Performance/load test console output
* `/api/metrics` observations

Screenshots and additional evidence should be attached to the corresponding defect records where applicable.

---

# 8. Overall Test Case Conclusion

The implemented UI and API automation successfully validated authentication for all seven documented user accounts.

* **UI Login:** 7/7 Passed
* **API Login:** 7/7 Passed
* **Metrics API:** Passed
* **Performance/Load:** Pass with observations

The detailed business workflows identified in the scenario matrix were not fully executed within the assessment time window and are explicitly marked as pending rather than being reported as passed.

This distinction ensures that the reported results represent actual test execution and do not overstate the level of application coverage.
