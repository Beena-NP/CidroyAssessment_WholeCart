# WholeCart B2B Marketplace – End-to-End Scenario Matrix

## Purpose

This matrix identifies the major end-to-end business scenarios across the Buyer, Seller and Operator roles. It is used to define functional test coverage and identify critical cross-role workflows.

## Buyer Scenarios

| ID     | Scenario                                      | Expected Outcome                                                            | Priority |
| ------ | --------------------------------------------- | --------------------------------------------------------------------------- | -------- |
| BUY-01 | Buyer logs in with valid credentials          | Buyer is authenticated and lands on the Catalogue page                      | High     |
| BUY-02 | Buyer logs in with invalid credentials        | Login is rejected with an appropriate error message                         | High     |
| BUY-03 | Buyer views product catalogue                 | Available products and relevant product information are displayed           | High     |
| BUY-04 | Buyer selects a product                       | Product details are displayed correctly                                     | High     |
| BUY-05 | Buyer adds product to cart                    | Product is added with the selected quantity                                 | High     |
| BUY-06 | Buyer updates cart quantity                   | Cart total and quantity are recalculated correctly                          | High     |
| BUY-07 | Buyer removes product from cart               | Product is removed and cart totals are updated                              | Medium   |
| BUY-08 | Buyer proceeds to checkout                    | Checkout page displays correct order information                            | High     |
| BUY-09 | Buyer places a B2B order                      | Order is created successfully with a unique order reference                 | Critical |
| BUY-10 | Buyer views order status                      | Correct order status is displayed                                           | High     |
| BUY-11 | Buyer views generated tax invoice             | Invoice contains correct transaction and buyer information                  | Critical |
| BUY-12 | Buyer validates CR-09 invoice fields          | Supply Type B2B and Place of Supply state name/code are present and correct | Critical |
| BUY-13 | Buyer attempts to access seller functionality | Access is denied/restricted                                                 | High     |

## Seller Scenarios

| ID     | Scenario                                                          | Expected Outcome                                                   | Priority |
| ------ | ----------------------------------------------------------------- | ------------------------------------------------------------------ | -------- |
| SEL-01 | Seller logs in with valid credentials                             | Seller is authenticated and lands on the Seller Orders page        | High     |
| SEL-02 | Seller logs in with invalid credentials                           | Login is rejected with an appropriate error message                | High     |
| SEL-03 | Seller views incoming orders                                      | Relevant buyer orders are displayed                                | Critical |
| SEL-04 | Seller opens an order                                             | Correct order and buyer details are displayed                      | High     |
| SEL-05 | Seller updates order status                                       | Valid status transition is saved successfully                      | Critical |
| SEL-06 | Seller attempts invalid order-status transition                   | Invalid transition is prevented or appropriate validation is shown | High     |
| SEL-07 | Seller views order details after status update                    | Updated status is reflected correctly                              | High     |
| SEL-08 | Seller attempts to access operator functionality                  | Access is denied/restricted                                        | High     |
| SEL-09 | Seller attempts to access another seller's restricted information | Unauthorized data is not exposed                                   | Critical |

## Operator Scenarios

| ID     | Scenario                                                    | Expected Outcome                                         | Priority |
| ------ | ----------------------------------------------------------- | -------------------------------------------------------- | -------- |
| OPR-01 | Operator logs in with valid credentials                     | Operator is authenticated and lands on the Settings page | High     |
| OPR-02 | Operator logs in with invalid credentials                   | Login is rejected with an appropriate error message      | High     |
| OPR-03 | Operator accesses operator functionality                    | Authorized functionality is available                    | High     |
| OPR-04 | Operator attempts to access buyer/seller-only functionality | Access is restricted where applicable                    | High     |
| OPR-05 | Operator views marketplace operational information          | Relevant information is displayed correctly              | Medium   |

## Cross-Role End-to-End Scenarios

| ID     | End-to-End Flow                                        | Roles                 | Expected Outcome                                                              | Priority |
| ------ | ------------------------------------------------------ | --------------------- | ----------------------------------------------------------------------------- | -------- |
| E2E-01 | Buyer login → Catalogue → Product selection → Cart     | Buyer                 | Buyer can successfully build a cart                                           | Critical |
| E2E-02 | Buyer → Checkout → Place B2B Order                     | Buyer                 | Order is created successfully                                                 | Critical |
| E2E-03 | Buyer creates order → Seller views order               | Buyer → Seller        | Seller can view the newly created order                                       | Critical |
| E2E-04 | Seller updates order status → Buyer views order        | Seller → Buyer        | Updated order status is visible to the buyer                                  | Critical |
| E2E-05 | Buyer completes order → Invoice generated              | Buyer/System          | Tax invoice is generated with correct information                             | Critical |
| E2E-06 | B2B order → Invoice → CR-09 validation                 | Buyer/System          | Supply Type B2B and Place of Supply state name/code are correctly represented | Critical |
| E2E-07 | Buyer attempts Seller/Operator access                  | Buyer                 | Unauthorized functionality is blocked                                         | High     |
| E2E-08 | Seller attempts Buyer/Operator access                  | Seller                | Unauthorized functionality is blocked                                         | High     |
| E2E-09 | Operator accesses authorized operational functionality | Operator              | Operator can perform permitted activities                                     | High     |
| E2E-10 | Multiple users operate on marketplace workflows        | Buyer/Seller/Operator | Role isolation and transaction consistency are maintained                     | High     |

## Negative / Boundary Scenarios

| ID     | Scenario                                         | Expected Outcome                                             | Priority |
| ------ | ------------------------------------------------ | ------------------------------------------------------------ | -------- |
| NEG-01 | Invalid username/password                        | Authentication fails without exposing sensitive information  | High     |
| NEG-02 | Empty username/password                          | Appropriate mandatory-field validation is displayed          | Medium   |
| NEG-03 | Buyer accesses seller endpoint/functionality     | Request is rejected or access is denied                      | Critical |
| NEG-04 | Seller accesses another seller's restricted data | Unauthorized information is not accessible                   | Critical |
| NEG-05 | Product quantity exceeds allowed limit           | Application prevents invalid quantity                        | Medium   |
| NEG-06 | Product becomes unavailable before checkout      | Order cannot be created with unavailable product             | High     |
| NEG-07 | Order submission is repeated                     | Duplicate order creation is prevented                        | Critical |
| NEG-08 | Invalid/missing GSTIN information                | Appropriate validation is applied                            | High     |
| NEG-09 | Invoice missing CR-09 mandatory information      | Invoice is rejected/flagged as invalid                       | Critical |
| NEG-10 | API request exceeds documented rate limit        | API returns appropriate rate-limit response such as HTTP 429 | High     |

## Coverage Summary

The scenario matrix covers:

* Buyer authentication and purchasing workflows
* Seller order-management workflows
* Operator access and operational functionality
* Cross-role order lifecycle
* B2B invoice and CR-09 requirements
* Role-based access control
* Negative and boundary conditions
* API rate limiting
* Transaction consistency and duplicate-order prevention

The detailed test cases and execution results will be maintained separately.
