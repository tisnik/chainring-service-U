Feature: Smoke test

  @smoketest
  Scenario: Check the API entry point
    Given System is running
    When I access the API endpoint /
    Then I should get 200 status code

  @smoketest
  Scenario Outline: Check that the API entry point returns list of all endpoints
    Given System is running
    When I access the API endpoint /
    Then I should get 200 status code
     And I should get proper JSON data
     And I should find key <key> in received data

     Examples: keys
     |key|
     |/api/v1/|
     |/api/v1/info|
     |/api/v1/config|
     |/api/v1/drawing|
     |/api/v1/drawings-cache|
     |/api/v1/readiness|
     |/api/v1/project-list|
     |/api/v1/project|
     |/api/v1/building|

  @smoketest
  Scenario: Check the API entry point /info
    Given System is running
    When I access the API endpoint /info
    Then I should get 200 status code
     And I should get proper JSON data
     And I should find the value Chainring Service under the key name
