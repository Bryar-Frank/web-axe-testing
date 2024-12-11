Feature: Testing Google With Search "puppy"

  Scenario: Testing Accessibility Standards
    Given a certain webpage "https://www.google.com/search?q=puppy"
    When "<type>" accessibility checks are performed
    Then there are no violations

    Examples:
      | type           |
      | Full           |
      | WCAG Only      |
      | Best Practices |
