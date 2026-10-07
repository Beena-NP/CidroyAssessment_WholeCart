# WholeCart B2B Marketplace – Test Plan

## 1. Objective

Validate the functional correctness, role-based access, API behaviour, critical business workflows and basic performance characteristics of the WholeCart B2B Marketplace.

The testing will focus on identifying business-critical defects and providing confidence in the application's readiness for the assessment release.

## 2. Scope

### In Scope

* Buyer, Seller and Operator login
* Role-based landing pages and access
* Product catalogue and product selection
* Cart and checkout workflows
* Order creation and order status
* Seller order management
* Operator/admin functionality available in the environment
* B2B tax invoice generation and validation
* CR-09 invoice requirements
* API authentication and response validation
* API negative scenarios
* Basic performance/load testing up to 50 RPS
* Validation using `/api/metrics`
* Defect identification, evidence and reporting

### Out of Scope

* Production-scale performance certification
* Infrastructure/network capacity testing
* Third-party systems that are unavailable in the assessment environment
* Security penetration testing
* Long-duration endurance/soak testing

## 3. Test Approach

Testing will use a combination of:

* **UI functional testing** – Selenium WebDriver with Java and TestNG
* **API testing** – Rest Assured
* **Manual exploratory testing** – business workflows and negative scenarios
* **Data-driven testing** – TestNG DataProvider for multiple user accounts
* **Performance/load testing** – controlled request generation at 10, 25, 40 and 50 RPS
* **API monitoring** – `/api/metrics` for request count, 5xx errors, p50, p95 and orders in flight
* **Defect reporting** – reproducible steps, expected/actual results and evidence

## 4. Test Data

The documented Buyer, Seller and Operator test accounts will be used for authentication and role-based testing.

Test data will also cover:

* Valid and invalid credentials
* Buyer and seller transactions
* Product and quantity variations
* B2B buyer GSTIN/state combinations
* Invoice data required by CR-09

Credentials and sensitive values will be maintained outside source code using environment variables and will not be committed to GitHub.

## 5. Entry Criteria

* Application environment is accessible.
* FDD and API documentation are available.
* Test accounts are available.
* Required automation dependencies are configured.
* Application is sufficiently stable for functional testing.

## 6. Exit Criteria

Testing will be considered complete when:

* Planned critical workflows have been executed.
* Critical/high-severity defects are documented.
* UI and API automation for the selected scope is completed.
* Performance observations have been documented.
* Test results and known limitations are reported.

## 7. Risks & Mitigations

| Risk                                                   | Mitigation                                                                       |
| ------------------------------------------------------ | -------------------------------------------------------------------------------- |
| Requirements are incomplete or ambiguous               | Maintain Questions & Assumptions Log                                             |
| Test environment may differ from production            | Clearly document environment limitations                                         |
| External integrations may be unavailable               | Test only observable integration behaviour                                       |
| Performance results may vary due to shared environment | Repeat critical observations where time permits and report results as indicative |
| Sensitive credentials could be exposed                 | Use environment variables and exclude secrets from Git                           |

## 8. Deliverables

* Questions & Assumptions Log
* Test Plan
* End-to-End Scenario Matrix
* Test Cases and Results
* Defect Log with Evidence
* UI/API Automation
* Performance & Load Test Report
* Overall Test Results Report
* Assessment voice recording covering key defects and an AI-tool mistake
