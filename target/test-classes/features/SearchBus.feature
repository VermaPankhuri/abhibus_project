Feature: Bus Search
  As a user of AbhiBus
  I want to search for buses between two cities
  So that I can find available bus services for my travel

  Scenario: Search buses from Mumbai to Pune
    Given User opens Abhibus website
    When User enters "Mumbai" in Leaving From
    And User enters "Pune" in Going To
    And User selects travel date
    And User clicks Search
    Then Bus results should appear
    And User selects AC filter
    And User selects Cheapest first filter
    And User selects departure time filter