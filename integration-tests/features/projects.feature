Feature: Tests REST API endpoints that relates to projects handling

  @smoketest
  Scenario Outline: Check the project list
    Given System is running
    When I read project list
    Then I should get 200 status code
     And I should get proper JSON data
     And I should find 3 projects
     And I should find project with ID=<ID>, AOID=<AOID>, and name=<Name>

     Examples: projects
     | ID | AOID     | Name    |
     | 1  | SAP10000 | Areal 1 |
     | 2  | SAP20000 | Areal 2 |
     | 3  | SAP30000 | Areal 3 |

  @smoketest
  Scenario Outline: Check the projects
    Given System is running
    When I read projects
    Then I should get 200 status code
     And I should get proper JSON data
     And I should find 3 projects
     And I should find project with ID=<ID>, AOID=<AOID>, and name=<Name>

     Examples: projects
     | ID | AOID     | Name    |
     | 1  | SAP10000 | Areal 1 |
     | 2  | SAP20000 | Areal 2 |
     | 3  | SAP30000 | Areal 3 |
