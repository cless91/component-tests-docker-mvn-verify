Feature: Component-test starter

  Scenario: The application is correctly started
    Given the application is up
    And the database is empty
    When the following "CREATE USER" REST request is sent:
      | name        | joseph           |
      | mail        | toto@yopmail.com |
      | phoneNumber | 083665656        |