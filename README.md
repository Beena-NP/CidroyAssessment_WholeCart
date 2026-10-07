# CidroyAssessment_WholeCart

QA Automation assessment for the **WholeCart B2B Marketplace**.

## Tech Stack

* Java
* Selenium WebDriver
* TestNG
* Rest Assured
* Maven
* Git & GitHub

## Automation

### UI Automation

* Login validation for Buyer, Seller and Operator accounts
* TestNG DataProvider used for multiple accounts
* Role-based landing page validation

### API Automation

* Login API validation for all 7 accounts
* HTTP status code validation
* Token presence validation
* Role validation
* User name validation

## Configuration

Application URL is maintained in:

`src/main/resources/testdata.properties`

Login credentials are stored using local environment variables and are not committed to GitHub.

## Test Execution

Run all tests using:

```bash
mvn clean test
```

Tests can also be executed directly from IntelliJ IDEA.

## Test Result

UI Login Tests: **7 Passed**

API Login Tests: **7 Passed**
