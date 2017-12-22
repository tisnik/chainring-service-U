Feature: Smoke test

  @smoketest
  Scenario: Check the API entry point
    Given System is running
    When I wait 0 seconds
    Then I wait 0 seconds

  @smoketest
  Scenario: Check the API entry point
    Given System is running
    When I access the API endpoint /
    Then I should get 200 status code
