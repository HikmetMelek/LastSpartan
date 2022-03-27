Feature: Test Spartan API with complete CRUD operations

  Scenario: Read data from Spartan API
    Given User sends a request to Mock API for a mock Spartan Data
    When User uses Mock Data to create a Spartan
    And User send a request to Spartan API with ID 0
    Then created Spartan has same info with post request
    And User update all the fields of created Spartan
    Then User deletes spartan 0

