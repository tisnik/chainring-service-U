Feature: Tests for common REST API endpoints

  @smoketest
  Scenario: Check the /liveness API entry point
    Given System is running
    When I access the API endpoint /liveness
    Then I should get 200 status code
     And I should get proper JSON data
     And I should find the following keys (status) in the JSON response
     And I should find the value "ok" under the key "status"

  @smoketest
  Scenario: Check the /readiness API entry point
    Given System is running
    When I access the API endpoint /readiness
    Then I should get 200 status code
     And I should get proper JSON data
     And I should find the value "ok" under the key "status"

  Scenario Outline: Check the API entry point /info
    Given System is running
    When I access the API endpoint /info
    Then I should get 200 status code
     And I should get proper JSON data
     And I should find the value "<value>" under the key "<key>"

     Examples: values
     |key            |value            |
     |name           |Chainring Service|
     |service-version|0.1.0            |
     |db-version     |1                |
     |api-prefix     |/api             |
     |api-version    |v1               |
     |full-prefix    |/api/v1          |

  Scenario: Check the API entry point /config
    Given System is running
    When I access the API endpoint /config
    Then I should get 200 status code
     And I should get proper JSON data
     And I should find key "configuration" in received data

