# CidroyAssessment_WholeCart
WholeCart B2B Marketplace – QA Automation Assessment | UI, API, Regression &amp; Performance Testing
CidroyAssessment_WholeCart
Overview

QA Automation assessment for the WholeCart B2B Marketplace application.

Tech Stack
Java
Selenium WebDriver
TestNG
Maven
IntelliJ IDEA
Git & GitHub
Current Automation

The project currently includes:

Buyer login UI automation
Page Object Model (POM)
Configuration management using testdata.properties
Secure handling of login credentials using environment variables
TestNG test execution
Configuration

Application URL is maintained in:

src/main/resources/testdata.properties

Buyer credentials are stored as local environment variables:

BUYER_USERNAME
BUYER_PASSWORD

Credentials are not stored in the GitHub repository.

Test Execution

Run the tests using:

mvn clean test

Tests can also be executed directly from IntelliJ IDEA.

Test Result

The buyer login automation has been successfully executed.

Result: PASS
