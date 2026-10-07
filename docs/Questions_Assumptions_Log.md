# Questions & Assumptions Log

## Purpose

This document captures key questions, ambiguities and assumptions identified while reviewing the WholeCart B2B Marketplace requirements and API documentation.

The assumptions were used to define the test scope and expected behaviour where explicit clarification was not available.

## Questions and Assumptions

| ID | Area | Question / Observation | Assumption / Decision | Impact |
|---|---|---|---|---|
| QA-01 | User Roles | The application supports Buyer, Seller and Operator roles. Are role permissions fully defined for every functional area? | Role-based access will be validated for the documented Buyer, Seller and Operator accounts. | Medium |
| QA-02 | Login | Is there a requirement for account lockout after multiple failed login attempts? | No lockout requirement was specified, so negative login testing will focus on invalid credentials and appropriate error handling. | Medium |
| QA-03 | Authentication | The API returns a Bearer token after successful login. What is the token expiry duration? | Token expiry duration is not specified. Token presence and successful authentication will be validated. | Medium |
| QA-04 | API Security | Seller partner APIs require an `X-Api-Key`. How are API keys provisioned and rotated? | Valid API-key handling will be tested where test credentials are available. Secrets will not be committed to the repository. | High |
| QA-05 | Product Catalogue | What is the expected behaviour when a product becomes unavailable after being added to the cart? | The application should prevent checkout of unavailable products and provide an appropriate message. | Medium |
| QA-06 | Cart | Are quantity limits defined for individual products or orders? | No specific quantity limit was identified. Boundary testing will be based on application behaviour and available requirements. | Medium |
| QA-07 | Order | What happens if an order submission is interrupted after payment/order confirmation is initiated? | The system should avoid duplicate order creation and maintain a consistent order state. | High |
| QA-08 | Tax Invoice / CR-09 | CR-09 requires Supply Type B2B and Place of Supply state name/code based on the buyer GSTIN. | Invoice validation will verify the presence and correctness of these fields for B2B transactions. | High |
| QA-09 | GSTIN | Is GSTIN format validation and state-code validation explicitly defined? | GSTIN validation will be checked against the documented business expectation and available application behaviour. | High |
| QA-10 | Invoice | CR-09 states that missing required invoice information can make the invoice invalid for ITC. | Missing Supply Type or Place of Supply information will be treated as a high-severity invoice defect. | High |
| QA-11 | Invoice Seal | CR-09 specifies the seal text `APPROVED CCB . WC`. Is the exact spacing/punctuation mandatory? | The exact documented text will be treated as the expected value. | Medium |
| QA-12 | API Rate Limit | API documentation specifies a maximum target of 50 requests per second. Is this a hard system limit or a recommended load target? | 50 RPS was treated as the maximum documented target. HTTP 429 responses at this threshold were recorded as rate-limiting behaviour. | High |
| QA-13 | Performance | What are the expected response-time SLAs for individual APIs? | No explicit SLA was available. Performance results are therefore reported as observations rather than SLA compliance. | High |
| QA-14 | Performance Metrics | `/api/metrics` exposes a 60-second rolling window. | Metrics were used for monitoring request count, 5xx errors, p50, p95 and orders in flight during load testing. | Medium |
| QA-15 | Load Testing | Should all business APIs be included in the load test? | Due to the assessment time constraint, the controlled load test focused on the login API and used `/api/metrics` for monitoring. | Medium |
| QA-16 | Test Data | Are dedicated test users and transactional test data available for all workflows? | The documented Buyer, Seller and Operator accounts were used for authentication testing. Additional workflow data was assumed to be available through the application. | Medium |
| QA-17 | Concurrent Users | Is there a defined expected number of concurrent users for production? | No concurrent-user target was specified. Load was therefore expressed in requests per second. | Medium |
| QA-18 | Environment | Is the assessment environment representative of production infrastructure? | The provided WholeCart environment was treated as the test environment. Results should not be considered production capacity certification. | High |
| QA-19 | External Integrations | Are payment, notification, shipping or other third-party integrations available in the assessment environment? | Only functionality accessible in the provided environment was considered in scope. External dependency behaviour was not assumed unless observable. | Medium |
| QA-20 | Defect Handling | What is the expected severity/priority classification standard? | Standard QA severity and priority classification will be used based on business impact and user impact. | Low |

## Key Assumptions

1. The provided WholeCart environment and API documentation represent the intended assessment baseline.

2. The documented Buyer, Seller and Operator accounts are valid test accounts.

3. Credentials and API keys are considered sensitive and must not be stored directly in source code or committed to GitHub.

4. Successful login is expected to return HTTP 200 and a valid authentication token.

5. Role-based access restrictions are expected to prevent users from accessing functionality outside their permitted role.

6. CR-09 requirements are considered part of Release 1.0 and therefore are included in invoice validation.

7. The exact CR-09 invoice requirements are:
    - Supply Type: B2B
    - Place of Supply: state name and state code derived from the buyer GSTIN
    - Required approval seal text: `APPROVED CCB . WC`

8. The documented 50 RPS value is treated as the maximum load target for the assessment.

9. HTTP 429 responses received at the documented rate limit are considered rate-limiting behaviour unless requirements indicate otherwise.

10. Performance results from the assessment environment are indicative only and should not be treated as production capacity certification.

## Open Items Requiring Product Owner Clarification

The following items would ideally require clarification before production release:

- API response-time SLAs and performance acceptance criteria.
- Token expiry and refresh behaviour.
- Account lockout and authentication security requirements.
- Detailed role-permission matrix.
- GSTIN validation rules.
- Order/payment failure and duplicate-order handling.
- Expected concurrent-user and transaction-volume targets.
- Whether the 50 RPS limit is a hard limit or a recommended operating threshold.
- Availability and behaviour of external integrations.