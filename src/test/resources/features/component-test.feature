Feature: Component-test starter

  Background:
    Given the application is up and ready
    And the database is empty

  Scenario: Create contact
    When the following "CREATE CONTACT" REST request is sent:
      | name        | joseph           |
      | email       | toto@yopmail.com |
      | phoneNumber | 083665656        |
    Then the following contact is present in the database:
      | name        | joseph           |
      | email       | toto@yopmail.com |
      | phoneNumber | 083665656        |